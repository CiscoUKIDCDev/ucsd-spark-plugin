/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.inputs;

import java.util.List;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.json.SparkRoom;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.InfraAccount;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Provides a tabular list of Spark Rooms from all accounts for a user to select
 * from
 *
 * @author Matt Day
 *
 */
public class SparkRoomSelector implements TabularReportGeneratorIf {

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		final TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		final ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
		final List<InfraAccount> objs = store.queryAll();

		final TabularReportInternalModel model = new TabularReportInternalModel();
		// The internal ID is hidden here. It will set the context for your
		// action buttons and drilldown reports
		model.addTextColumn("Internal ID", "Internal ID", true);
		model.addTextColumn("Account", "Account");
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Type", "Type");
		model.addTextColumn("Locked", "Locked");
		model.addTextColumn("Last activity", "Last activity");
		model.addTextColumn("Created", "Created");
		model.completedHeader();
		/*
		 * Loop through every account UCS Director has and match only on Spark
		 * accounts
		 */
		for (final InfraAccount a : objs) {
			final PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
			// Important to check if the account type is null first
			if ((acc != null) && (acc.getAccountType() != null)
					&& (acc.getAccountType().equals(SparkConstants.INFRA_ACCOUNT_TYPE))) {
				SparkAccount account = new SparkAccount(acc.getAccountName());

				for (SparkRoom room : SparkInventory.getRooms(account).getItems()) {

					// Generate internal ID in the format
					// AccountName;RoomID;Room Name
					final String internalId = account.getAccountName() + ";" + room.getId() + ";" + room.getTitle();

					/*
					 * Now add the various attributes as per above (the number
					 * of entries must match the number of column headers or it
					 * will throw an exception)
					 */
					model.addTextValue(internalId);
					model.addTextValue(account.getAccountName());
					model.addTextValue(room.getTitle());
					model.addTextValue(room.getType());
					model.addTextValue(room.getIsLocked().toString());
					model.addTextValue(room.getLastActivity());
					model.addTextValue(room.getCreated());
					model.completedRow();

				}
			}
		}
		model.updateReport(report);

		return report;
	}

}
