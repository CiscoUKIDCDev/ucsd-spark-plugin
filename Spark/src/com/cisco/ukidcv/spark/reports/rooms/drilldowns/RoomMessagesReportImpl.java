/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.rooms.drilldowns;

import java.util.HashMap;
import java.util.Map;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.json.SparkMembership;
import com.cisco.ukidcv.spark.api.json.SparkMessageFormat;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Shows a tabular report of the most recent messages
 */
public class RoomMessagesReportImpl implements TabularReportGeneratorIf {

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
		model.addTextColumn("Time", "Time");
		model.addTextColumn("Sender", "Sender");
		model.addTextColumn("Message", "Message");
		model.completedHeader();

		final SparkAccount account = new SparkAccount(context);
		final String roomId = context.getId().split(";")[1];

		Map<String, String> userMap = new HashMap<>();

		// The Spark messages API doesn't include usernames, look them up from
		// the room membership list:
		for (SparkMembership m : SparkApi.getSparkRoomMemberships(account, roomId).getItems()) {
			userMap.put(m.getPersonId(), m.getPersonDisplayName());
		}

		// Loop through all room messages - this query is done live as a JSON
		// call and is not cached (and thus may be slower)
		for (SparkMessageFormat m : SparkApi.getMessages(account, roomId).getItems()) {
			// Generate internal ID in the format AccountName;RoomID;MessageId
			final String internalId = account.getAccountName() + ";" + m.getRoomId() + ";" + m.getId();

			/*
			 * Now add the various attributes as per above (the number of
			 * entries must match the number of column headers or it will throw
			 * an exception)
			 */
			model.addTextValue(internalId);
			model.addTextValue(m.getCreated());
			model.addTextValue(userMap.get(m.getPersonId()));
			model.addTextValue(m.getMessage());
			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
