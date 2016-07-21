/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.teams.drilldowns;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.json.SparkTeamMembership;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Shows a tabular report of all team members and a bit of detail on each of
 * them
 */
public class TeamMembersReportImpl implements TabularReportGeneratorIf {

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
		model.addTextColumn("Created", "Created");
		model.completedHeader();

		final SparkAccount account = new SparkAccount(context);
		final String teamId = context.getId().split(";")[1];

		// Loop through all team members - this query is done live as a JSON
		// call and is not cached (and thus may be slower)
		for (SparkTeamMembership member : SparkApi.getSparkTeamMemberships(account, teamId).getItems()) {
			// Generate internal ID in the format AccountName;TeamID;Team Name
			final String internalId = account.getAccountName() + ";" + member.getId() + ";" + member.getPersonEmail();

			/*
			 * Now add the various attributes as per above (the number of
			 * entries must match the number of column headers or it will throw
			 * an exception)
			 */
			model.addTextValue(internalId);
			model.addTextValue(member.getPersonDisplayName());
			model.addTextValue(member.getPersonEmail());
			model.addTextValue(member.isModerator().toString());
			model.addTextValue(member.getCreated());
			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
