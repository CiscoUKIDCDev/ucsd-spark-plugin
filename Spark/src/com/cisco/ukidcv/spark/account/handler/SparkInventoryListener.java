/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.handler;

import com.cloupia.lib.connector.InventoryContext;
import com.cloupia.lib.connector.InventoryEventListener;
import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.PhysicalAccountManager;
import com.cloupia.lib.connector.account.PhysicalConnectivityStatus;

/**
 * Boilerplate from SDK - not sure what it does
 *
 * @author Matt Day
 *
 */
public class SparkInventoryListener implements InventoryEventListener {
	@Override
	public void afterInventoryDone(String accountName, InventoryContext ctx) throws Exception {
		SparkAccountPersistenceUtil.persistCollectedInventory(accountName);

		final AccountTypeEntry entry = PhysicalAccountManager.getInstance().getAccountTypeEntryByName(accountName);
		@SuppressWarnings("unused")
		PhysicalConnectivityStatus connectivityStatus = null;
		if (entry != null) {
			connectivityStatus = entry.getTestConnectionHandler().testConnection(accountName);
		}

	}

	@Override
	public void beforeInventoryStart(String accountName, InventoryContext ctx) throws Exception {
		// Do nothing

	}

}
