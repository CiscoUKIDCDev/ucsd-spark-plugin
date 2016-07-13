/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.rooms.drilldowns;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.json.SparkRoom;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.SummaryReportInternalModel;

/**
 * Shows a summary report about a particular Spark room
 * 
 * @author Matt Day
 *
 */
public class RoomSummaryReportImpl implements TabularReportGeneratorIf {

	private static final String[] GROUP_ORDER = {
			SparkConstants.OVERVIEW_LABEL
	};

	/**
	 * This method returns implemented tabular report,and also perform cleanup
	 * process and updating report.
	 *
	 * @param reportEntry
	 *            This parameter contains Object of ReportRegistryEntry class
	 *            which is used to register newly created report
	 * @param context
	 *            This parameter contains context of the report
	 * @return report
	 */
	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();
		report.setContext(context);
		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());

		SummaryReportInternalModel model = new SummaryReportInternalModel();

		SparkAccount account = new SparkAccount(context);

		String roomId = context.getId().split(";")[1];

		SparkRoom room = SparkInventory.getRoom(account, roomId);

		// Build the table
		model.addText("Room Name", room.getTitle(), SparkConstants.OVERVIEW_LABEL);
		model.addText("Type", room.getType(), SparkConstants.OVERVIEW_LABEL);
		model.addText("Last Activity", room.getLastActivity(), SparkConstants.OVERVIEW_LABEL);
		model.addText("Created", room.getCreated(), SparkConstants.OVERVIEW_LABEL);
		model.addText("Locked", room.getIsLocked().toString(), SparkConstants.OVERVIEW_LABEL);

		// finally perform last clean up steps
		model.setGroupOrder(GROUP_ORDER);
		model.updateReport(report);

		return report;
	}

}
