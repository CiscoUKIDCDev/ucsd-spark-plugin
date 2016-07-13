/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.summary;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

/**
 * This provides a summary table in the account overview window next to the
 * graphs.
 *
 * @author Matt Day
 *
 */
public class SummaryTable extends CloupiaNonTabularReport {

	/**
	 * Unique identifier for this report - each report ID needs to be unique
	 */
	public final static String REPORT_NAME = "com.cisco.ukidcv.spark.reports.summary.SummaryTable";

	/**
	 * Every report has two components - description and implementation. Tell
	 * UCSD what the implementation class is
	 *
	 * @return implementation class
	 */
	@Override
	public Class<SummaryTableImpl> getImplementationClass() {
		return SummaryTableImpl.class;
	}

	/**
	 * Create overview table
	 */
	public SummaryTable() {
		super();
	}

	/**
	 * This method returns the report label to be display in UI
	 *
	 * @return label of report
	 */
	@Override
	public String getReportLabel() {
		return SparkConstants.SUMMARY_LABEL;
	}

	/**
	 * I'm not sure what this does, but the SDK says it's important to override
	 * the default..?
	 */
	@Override
	public int getMenuID() {
		return 51;
	}

	/**
	 * @return This method returns report name, each report should have unique
	 *         name
	 */
	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	/**
	 * @return This report returns boolean value true/false. it returns false if
	 *         report is not easy(report has implementation class)
	 */
	@Override
	public boolean isEasyReport() {
		return false;
	}

	/**
	 * @return This report returns boolean value true/false. Returns true if it
	 *         is leaf report otherwise it returns false
	 */
	@Override
	public boolean isLeafReport() {
		return false;
	}

	/**
	 * @return This method returns type of report like summary/pie chart/Line
	 *         chart/tabular etc
	 */
	@Override
	public int getReportType() {
		return ReportDefinition.REPORT_TYPE_SUMMARY;
	}

	/**
	 * @return This method returns type of report
	 */
	@Override
	public int getReportHint() {
		return ReportDefinition.REPORT_HINT_VERTICAL_TABLE_WITH_GRAPHS;
	}

	/**
	 * @return This method returns boolean value true/false. if it returns true
	 *         then only form will be shown in UI
	 */
	@Override
	public boolean isManagementReport() {
		return true;
	}

	/**
	 * Every report should have a context map telling UCSD what its parent is.
	 * In this case, map it back to the Spark account overview page.
	 */
	@Override
	public ContextMapRule[] getMapRules() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(SparkConstants.INFRA_ACCOUNT_TYPE);
		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());

		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;

		return rules;
	}

}
