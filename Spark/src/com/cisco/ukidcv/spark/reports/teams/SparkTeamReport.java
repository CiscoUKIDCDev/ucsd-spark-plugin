/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.teams;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.DrillableReportWithActions;

/**
 * Defines a team report - a list of all teams in the converged view.
 *
 * @author Matt Day
 * @see SparkTeamReportImpl
 *
 */
public class SparkTeamReport extends DrillableReportWithActions {
	/**
	 * Unique identifier for this report
	 */
	public final static String REPORT_NAME = "com.cisco.ukidcv.spark.reports.teams.SparkTeamReport";

	// Drilldown reports MUST be defined like this (i.e. not in the
	// getDrilldownReports method)
	private CloupiaReport[] drillable = new CloupiaReport[] {

	};

	private CloupiaReportAction[] actions = new CloupiaReportAction[] {

	};

	/**
	 * Create Team report
	 */
	public SparkTeamReport() {
		super();
		// This sets what column UCS Director will set as context when anything
		// is selected or for drilldown reports
		this.setMgmtColumnIndex(0);
		// This sets what column will show in the GUI when anything is selected
		this.setMgmtDisplayColumnIndex(1);
	}

	@Override
	public CloupiaReport[] getDrilldownReports() {
		return this.drillable;
	}

	@Override
	public Class<SparkTeamReportImpl> getImplementationClass() {
		return SparkTeamReportImpl.class;
	}

	@Override
	public CloupiaReportAction[] getActions() {
		return this.actions;
	}

	@Override
	public String getReportLabel() {
		return SparkConstants.TEAM_LABEL;
	}

	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	@Override
	public boolean isEasyReport() {
		return false;
	}

	@Override
	public boolean isLeafReport() {
		return false;
	}

	@Override
	public int getMenuID() {
		return 51;
	}

	// Whenever you're creating a drilldown report you must create a new context
	// level (unique for each report). Do it like this:
	@Override
	public int getContextLevel() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(SparkConstants.TEAM_LIST_DRILLDOWN);
		return context.getType();
	}

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
