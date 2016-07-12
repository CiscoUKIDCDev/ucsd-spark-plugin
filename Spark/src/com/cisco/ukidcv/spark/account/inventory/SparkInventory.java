/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.inventory;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.json.SparkRooms;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkAccountException;
import com.cisco.ukidcv.spark.exceptions.SparkReportException;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

/**
 * Runs every 60 minutes or after a user action (e.g. adding a city) and
 * collects information to store locally
 * <p>
 * This is handled in two parts - this inventory class providing static methods
 * and a database store
 *
 * @author Matt Day
 * @see com.cisco.ukidcv.spark.account.inventory.SparkInventoryDB
 *
 */
public class SparkInventory {
	static Logger logger = Logger.getLogger(SparkInventory.class);

	public static void update(SparkAccount account, String reason, boolean force) {

	}

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

	private static SparkInventoryDB create(SparkAccount account) throws Exception {
		PhysicalInfraAccount infraAccount = AccountUtil.getAccountByName(account.getAccountName());
		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(infraAccount);

		final String accountName = account.getAccountName();
		try {
			// Check we can reach the Spark API:
			if (SparkApi.testConnection(account)) {
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

	public static SparkRooms getRooms(SparkAccount account) throws HttpException, SparkReportException, IOException {
		// Update inventory if needed:
		update(account, SparkConstants.INVENTORY_REASON_PERIODIC, false);
		return SparkApi.getSparkRooms(SparkApi.getSparkRoomsJson(account));
	}
}
