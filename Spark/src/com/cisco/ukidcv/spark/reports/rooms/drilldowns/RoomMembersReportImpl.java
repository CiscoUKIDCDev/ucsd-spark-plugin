/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.rooms.drilldowns;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.json.SparkMembership;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Shows a tabular report of all room members and a bit of detail on each of
 * them
 */
public class RoomMembersReportImpl implements TabularReportGeneratorIf {

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();

		// The internal ID is hidden here. It will set the context for your
		// action buttons and drilldown reports
		model.addTextColumn("Internal ID", "Internal ID", true);
		model.addTextColumn("Name", "Name");
		model.addTextColumn("Email", "Email");
		model.addTextColumn("Moderator", "Moderator");
		model.addTextColumn("Monitor", "Monitor");
		model.addTextColumn("Created", "Created");
		model.completedHeader();

		final SparkAccount account = new SparkAccount(context);
		final String roomId = context.getId().split(";")[1];

		// Loop through all room members - this query is done live as a JSON
		// call and is not cached (and thus may be slower)
		for (SparkMembership member : SparkApi.getSparkRoomMemberships(account, roomId).getItems()) {
			// Generate internal ID in the format AccountName;RoomID;Room Name
			final String internalId = account.getAccountName() + ";" + member.getId() + ";"
					+ member.getPersonDisplayName();

			/*
			 * Now add the various attributes as per above (the number of
			 * entries must match the number of column headers or it will throw
			 * an exception)
			 */
			model.addTextValue(internalId);
			model.addTextValue(member.getPersonDisplayName());
			model.addTextValue(member.getPersonEmail());
			model.addTextValue(member.getIsModerator().toString());
			model.addTextValue(member.getIsMonitor().toString());
			model.addTextValue(member.getCreated());
			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
