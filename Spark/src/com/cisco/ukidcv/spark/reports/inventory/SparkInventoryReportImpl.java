/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.inventory;

import java.util.List;
import java.util.ListIterator;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Implements the log list
 *
 * @author Matt Day
 * @see SparkInventoryReport
 */
public class SparkInventoryReportImpl implements TabularReportGeneratorIf {

	@Override
	public TabularReport getTabularReportReport(ReportRegistryEntry reportEntry, ReportContext context)
			throws Exception {
		TabularReport report = new TabularReport();

		report.setGeneratedTime(System.currentTimeMillis());
		report.setReportName(reportEntry.getReportLabel());
		report.setContext(context);

		TabularReportInternalModel model = new TabularReportInternalModel();
		model.addTimeColumn("Start Time", "Start Time");
		model.addTextColumn("Type", "Type");
		model.addTextColumn("Comment", "Comment");
		model.addTimeColumn("End Time", "End Time");

		model.completedHeader();

		List<String> pollingList = SparkInventory.getLog(new SparkAccount(context));

		// Init iterator on last element to trawl backwards
		ListIterator<String> i = pollingList.listIterator(pollingList.size());

		// Reverse through list (bottom to top)
		while (i.hasPrevious()) {
			// Format:
			// Start@End@Forced@Comment
			String[] pollArray = i.previous().split("@");
			// Start time
			model.addTimeValue(Long.parseLong(pollArray[0]));
			if (pollArray[2].equals("false")) {
				model.addTextValue("Time-based update");
			}
			else {
				model.addTextValue("Event-based update");
			}
			model.addTextValue(pollArray[3]);
			// End time
			model.addTimeValue(Long.parseLong(pollArray[1]));

			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
