/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.handler;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.cisco.cuic.api.client.JSON;
import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.SparkAccountDB;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.cIaaS.netapp.model.StorageAccountStatus;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;

/**
 * This periodically polls the account to ensure everything is working. It tests
 * the /people/me API call to ensure it gets a response
 *
 * @author Matt Day
 *
 */
public class SparkAccountStatusSummary {
	private static Logger logger = Logger.getLogger(SparkAccountStatusSummary.class);

	/**
	 * Obtain account summary information
	 *
	 * @param accountName
	 * @throws Exception
	 */
	public static void accountSummary(String accountName) throws Exception {
		PhysicalInfraAccount acc = AccountUtil.getAccountByName(accountName);

		// Obtain internal account database to get the pod type
		String json = acc.getCredential();
		AbstractInfraAccount dbStore = (AbstractInfraAccount) JSON.jsonToJavaObject(json, SparkAccountDB.class);
		dbStore.setAccount(acc);

		PhysicalConnectivityStatus status = new PhysicalConnectivityStatus(acc);

		StorageAccountStatus accStatus = new StorageAccountStatus();
		accStatus.setAccountName(acc.getAccountName());
		accStatus.setDcName(dbStore.getPod());

		SparkAccount account = new SparkAccount(accountName);

		try {
			// Check we can reach the Spark API:
			if (SparkApi.testConnection(account)) {
				accStatus.setReachable(true);
				accStatus.setLastMessage("Connection OK");
				status.setConnectionOK(true);
			}
			else {
				accStatus.setReachable(false);
				status.setConnectionOK(false);
				accStatus.setLastMessage("Could not connect (check credentials)");
			}
		}
		// Other exceptions are probably IO etc which indicate failed
		// connectivity
		catch (@SuppressWarnings("unused") Exception e) {
			logger.warn("Connection failed: " + accountName);
			accStatus.setLastMessage("Could not connect (check proxy)");
			accStatus.setReachable(false);
			status.setConnectionOK(false);
		}

		accStatus.setLastUpdated(System.currentTimeMillis());

		accStatus.setModel(SparkConstants.INFRA_ACCOUNT_LABEL);
		accStatus.setServerAddress(dbStore.getServerAddress());
		accStatus.setVersion(SparkConstants.API_VERSION);
		persistSparkAccountStatus(accStatus);
	}

	/**
	 * Not sure what this does - adding from SDK boilerplate
	 *
	 * @param ac
	 * @throws Exception
	 */
	public static void persistSparkAccountStatus(StorageAccountStatus ac) throws Exception {
		PersistenceManager pm = ObjStoreHelper.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try {
			tx.begin();

			String query = "accountName == '" + ac.getAccountName() + "'";

			Query q = pm.newQuery(StorageAccountStatus.class, query);
			q.deletePersistentAll();

			pm.makePersistent(ac);
			tx.commit();
		}
		finally {
			try {
				if (tx.isActive()) {
					tx.rollback();
				}
			}
			finally {
				pm.close();
			}
		}
	}
}
