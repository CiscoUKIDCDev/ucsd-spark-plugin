/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.inventory;

import java.util.Date;

import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.json.SparkMemberships;
import com.cisco.ukidcv.spark.api.json.SparkPersonDetails;
import com.cisco.ukidcv.spark.api.json.SparkRooms;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkAccountException;
import com.cisco.ukidcv.spark.exceptions.SparkReportException;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.google.gson.Gson;

/**
 * Runs every 60 minutes or after a user action (e.g. adding a city) and
 * collects information to store locally
 * <p>
 * This is handled in two parts - this inventory class providing static methods
 * and a database store
 * <p>
 * This is a complex class and the heart of the plugin's communication with
 * Spark
 *
 * @author Matt Day
 * @see com.cisco.ukidcv.spark.account.inventory.SparkInventoryDB
 *
 */
public class SparkInventory {
	static Logger logger = Logger.getLogger(SparkInventory.class);

	/**
	 * Attempt to update the inventory.
	 * <p>
	 * UCS Director's internal inventory collection happens every 60 minutes.
	 * This is a long time. Whenever this is called it will check if a
	 * determined polling period has passed and if so, refresh the cached
	 * inventory
	 *
	 * @param account
	 *            Account to update
	 * @param reason
	 *            String reason for logging purposes
	 * @param force
	 *            set to true to force an update even if it's not due
	 * @throws Exception
	 */
	public static void update(SparkAccount account, String reason, boolean force) throws Exception {
		logger.info("Updating inventory");
		Date d = new Date();
		long c = d.getTime();
		// SparkInventoryDB invStore = getInventoryStore(account);
		final String accountName = account.getAccountName();
		final String queryString = "accountName == '" + accountName + "'";

		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(accountName);
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		try {

			ObjStore<SparkInventoryDB> invStoreCollection = ObjStoreHelper.getStore(SparkInventoryDB.class);
			SparkInventoryDB store = null;
			try {
				store = invStoreCollection.query(queryString).iterator().next();
			}
			catch (Exception e) {
				logger.warn("Possibly stale entry from older API - deleting & re-creating. " + e.getMessage());
				invStoreCollection.delete(queryString);
				store = create(account);
			}

			if (store == null) {
				logger.warn("Cannot find " + accountName + " in inventory! Rolling back and creating new");
				// Attempt to create it:
				store = create(account);
			}

			// Get user configured inventory lifespan (TODO fixme)
			final long inventoryLife = SparkConstants.MAX_POLLING_TIME;

			if ((!force) && ((d.getTime() - store.getUpdated()) < inventoryLife)) {
				return;
			}
			store.setUpdated(c);

			// Add room list to inventory
			logger.info("Updating room list");
			store.setRoomList(SparkApi.getSparkRooms(account));

			// Add user info to inventory
			logger.info("Updating user info");
			store.setMe(SparkApi.getSparkPerson(account));

			// Add membership inventory
			logger.info("Updating memberships");
			store.setMembershipList(SparkApi.getSparkMemberships(account));

			// Add membership inventory
			logger.info("Updating teams");
			store.setTeamList(SparkApi.getSparkTeams(account));

			d = new Date();
			final String update = c + "@" + d.getTime() + "@" + force + "@" + reason;

			log(store, update);

			invStoreCollection.modifySingleObject(queryString, store);

			status.setConnectionOK(true);

		}
		catch (Exception e) {
			logger.warn("Exception updating database! " + e.getMessage());

			status.setConnectionOK(false);
		}
	}

	/**
	 * Add a log entry
	 *
	 * @param store
	 *            Inventory store to log to
	 * @param message
	 *            Message to include
	 */
	private static void log(SparkInventoryDB store, String message) {
		store.getPolling().add(message);

		// Remove oldest entry if longer than the allowed log length
		if (store.getPolling().size() > SparkConstants.MAX_POLLING_LOG_ENTRIES) {
			store.getPolling().remove(0);
		}
	}

	/**
	 * Get the inventory database store
	 *
	 * @param account
	 *            Account to obtain
	 * @return Inventory DB
	 * @throws Exception
	 *             if there's a problem getting it
	 */
	private static SparkInventoryDB getInventoryStore(SparkAccount account) throws Exception {
		final String accountName = account.getAccountName();
		final String queryString = "accountName == '" + accountName + "'";
		try {

			ObjStore<SparkInventoryDB> invStoreCollection = ObjStoreHelper.getStore(SparkInventoryDB.class);

			for (SparkInventoryDB store : invStoreCollection.query(queryString)) {
				if (accountName.equals(store.getAccountName())) {
					return store;
				}
			}
		}
		catch (Exception e) {
			logger.warn("Exeption when doing this! " + e.getMessage());
		}
		// Account was deleted most likely during init
		return create(account);
	}

	/**
	 * Create a new inventory store
	 *
	 * @param account
	 *            Account to create it for
	 * @return New inventory store
	 * @throws Exception
	 *             if there's a problem creating it
	 */
	private static SparkInventoryDB create(SparkAccount account) throws Exception {
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(account.getAccountName());
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		final String accountName = account.getAccountName();
		try {
			// Check we can reach the Spark API:
			if (!SparkApi.testConnection(account)) {
				status.setConnectionOK(false);
				logger.warn("Could not get valid token - marking connection as invalid");
				throw new SparkAccountException("Could not get valid token");
			}
			ObjStore<SparkInventoryDB> invStoreCollection = ObjStoreHelper.getStore(SparkInventoryDB.class);
			SparkInventoryDB invStore = new SparkInventoryDB(accountName);
			invStoreCollection.insert(invStore);
			status.setConnectionOK(true);
			return invStore;
		}
		catch (Exception e) {
			status.setConnectionOK(false);
			logger.warn("Exeption when doing this! " + e.getMessage());
		}
		throw new SparkAccountException("Could not create inventory store!");
	}

	/**
	 * Gets a list of Spark rooms for the account requested. It will first check
	 * the cache needs updating.
	 * <p>
	 * To force updating the cache, use update(SparkAccount, String, boolean)
	 * setting the boolean to true before calling this.
	 *
	 * @param account
	 *            Account to check
	 * @return List of Spark rooms
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws Exception
	 *             If there's an issue reading or parsing the cache
	 * @see SparkRooms
	 * @see #update(SparkAccount, String, boolean)
	 */
	public static SparkRooms getRooms(SparkAccount account) throws SparkReportException, Exception {
		// Update inventory if needed:
		update(account, SparkConstants.INVENTORY_REASON_PERIODIC, false);

		SparkInventoryDB inv = getInventoryStore(account);
		String json = inv.getRoomList();

		// Check if the response is not empty:
		if (!"".equals(json)) {
			Gson gson = new Gson();
			SparkRooms rooms = gson.fromJson(json, SparkRooms.class);
			return rooms;
		}
		throw new SparkReportException("Could not parse JSON");
	}

	/**
	 * Gets a list of Spark memberships for the account requested. It will first
	 * check the cache needs updating.
	 * <p>
	 * To force updating the cache, use update(SparkAccount, String, boolean)
	 * setting the boolean to true before calling this.
	 *
	 * @param account
	 *            Account to check
	 * @return List of Spark memberships
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws Exception
	 *             If there's an issue reading or parsing the cache
	 * @see SparkRooms
	 * @see #update(SparkAccount, String, boolean)
	 */
	public static SparkMemberships getMemberships(SparkAccount account) throws SparkReportException, Exception {
		// Update inventory if needed:
		update(account, SparkConstants.INVENTORY_REASON_PERIODIC, false);

		SparkInventoryDB inv = getInventoryStore(account);
		String json = inv.getMembershipList();

		// Check if the response is not empty:
		if (!"".equals(json)) {
			Gson gson = new Gson();
			SparkMemberships memberships = gson.fromJson(json, SparkMemberships.class);
			return memberships;
		}
		throw new SparkReportException("Could not parse JSON");
	}

	/**
	 * Gets information about the user account
	 * <p>
	 * To force updating the cache, use update(SparkAccount, String, boolean)
	 * setting the boolean to true before calling this.
	 *
	 * @param account
	 *            Account to check
	 * @return Information about the user
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws Exception
	 *             If there's an issue reading or parsing the cache
	 * @see SparkPersonDetails
	 * @see #update(SparkAccount, String, boolean)
	 */
	public static SparkPersonDetails getMe(SparkAccount account) throws SparkReportException, Exception {
		// Update inventory if needed:
		update(account, SparkConstants.INVENTORY_REASON_PERIODIC, false);

		SparkInventoryDB inv = getInventoryStore(account);
		String json = inv.getMe();

		// Check if the response is not empty:
		if (!"".equals(json)) {
			Gson gson = new Gson();
			SparkPersonDetails me = gson.fromJson(json, SparkPersonDetails.class);
			return me;
		}
		throw new SparkReportException("Could not parse JSON");
	}
}
