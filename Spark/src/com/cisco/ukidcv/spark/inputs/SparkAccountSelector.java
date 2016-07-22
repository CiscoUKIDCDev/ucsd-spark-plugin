/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.inputs;

import java.util.List;

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
 * Provides a tabular list of Spark Accounts registered in UCS Director for a
 * user to select from
 *
 * @author Matt Day
 *
 */
public class SparkAccountSelector implements TabularReportGeneratorIf {

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
		model.addTextColumn("Account Name", "Account Name");
		model.addTextColumn("IP Address", "IP Address");
		model.addTextColumn("Pod", "Pod");
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
				model.addTextValue(a.getAccountName());
				model.addTextValue(a.getServer());
				model.addTextValue(a.getDcName());
				model.completedRow();
			}
		}
		model.updateReport(report);

		return report;
	}

}
