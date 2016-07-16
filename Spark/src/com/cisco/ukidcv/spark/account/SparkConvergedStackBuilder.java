/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account;

import java.util.ArrayList;
import java.util.List;

import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkAccountException;
import com.cloupia.model.cIM.ConvergedStackComponentDetail;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reports.contextresolve.ConvergedStackComponentBuilderIf;

/**
 * Implements the converged stack view - all the information on the right hand
 * side when you single-click the icon
 *
 * @author Matt Day
 *
 */
public class SparkConvergedStackBuilder implements ConvergedStackComponentBuilderIf {
	/**
	 * Overridden method from SDK
	 *
	 * @param contextId
	 *            account context Id
	 *
	 * @return returns ConvergedStackComponentDetail instance
	 */
	@Override
	public ConvergedStackComponentDetail buildConvergedStackComponent(String contextId)
			throws SparkAccountException, Exception {
		String accountName = null;
		if (contextId != null) {
			// As the contextId returns as: "account Name;POD Name"
			accountName = contextId.split(";")[0];
		}
		if (accountName == null) {
			throw new SparkAccountException("Unable to find the account name");
		}

		SparkAccount account = new SparkAccount(accountName);

		// Test connectivity to cloud
		boolean ok = false;
		try {
			// Check we can reach the Spark API:
			if (SparkApi.testConnection(account)) {
				ok = true;
			}
		}
		catch (@SuppressWarnings("unused") Exception e) {
			// Do nothing; status is already 'false'
		}

		// This builds the detail view on the right side of the converged view
		final ConvergedStackComponentDetail detail = new ConvergedStackComponentDetail();

		// Set some attributes there:
		detail.setModel(SparkConstants.INFRA_ACCOUNT_LABEL);
		detail.setOsVersion(SparkConstants.API_VERSION);
		detail.setVendorLogoUrl("/app/uploads/openauto/cloud.png");
		detail.setMgmtIPAddr(SparkConstants.SPARK_SERVER_HOSTNAME);
		detail.setStatus(ok ? "OK" : "Down");
		detail.setVendorName("Spark");
		detail.setIconUrl("/app/uploads/openauto/cloud.png");

		// setting account context type (required by UCSD...)
		detail.setContextType(
				ReportContextRegistry.getInstance().getContextByName(SparkConstants.INFRA_ACCOUNT_TYPE).getType());

		// Pass conext type to all reports:
		detail.setContextValue(contextId);
		// Not sure what '3' is here, guessing it's storage
		detail.setLayerType(ConvergedStackComponentDetail.LAYER_TYPE_VIRTUAL);

		// You can add arbitrary fields to this view like this:
		List<String> detailList = new ArrayList<>(2);
		detailList.add("User, " + SparkInventory.getMe(account).getDisplayName());
		detailList.add("Created, " + SparkInventory.getMe(account).getCreated());
		detail.setComponentSummaryList(detailList);

		return detail;
	}

}
