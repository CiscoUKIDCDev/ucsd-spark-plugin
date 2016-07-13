/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.summary;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.json.SparkPersonDetails;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.SummaryReportInternalModel;

/**
 * Implements a summary table and fills it with some sample values
 *
 * @author Matt Day
 *
 */
public class SummaryTableImpl implements TabularReportGeneratorIf {

	private static final String SYS_INFO_TABLE = SparkConstants.OVERVIEW_LABEL;

	private static final String[] GROUP_ORDER = {
			SYS_INFO_TABLE
	};

	/**
	 * This method returns a full tabular report with some sample data
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
		final SparkPersonDetails me = SparkInventory.getMe(account);

		// Build the table
		model.addText("Account Name", account.getAccountName(), SYS_INFO_TABLE);
		model.addText("User name", me.getDisplayName());
		for (String email : me.getEmails()) {
			model.addText("Email", email);
		}
		model.addText("Created", me.getCreated());

		// finally perform last clean up steps
		model.setGroupOrder(GROUP_ORDER);
		model.updateReport(report);

		return report;
	}
}
