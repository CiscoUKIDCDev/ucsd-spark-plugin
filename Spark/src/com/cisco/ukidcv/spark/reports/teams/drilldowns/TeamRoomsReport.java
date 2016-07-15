/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.teams.drilldowns;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.reports.teams.drilldowns.actions.CreateTeamRoomAction;
import com.cisco.ukidcv.spark.reports.teams.drilldowns.actions.DeleteTeamRoomAction;
import com.cisco.ukidcv.spark.reports.teams.drilldowns.actions.EditTeamRoomAction;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.DrillableReportWithActions;

/**
 * Team summary drill down report.
 * <p>
 * Like any other summary report it shows an overview of a particular team
 * including graphs and so on
 *
 * @author Matt Day
 *
 */
public class TeamRoomsReport extends DrillableReportWithActions {

	private final static String REPORT_NAME = "com.cisco.ukidcv.spark.reports.teams.drilldowns.TeamRoomsReport";

	private CloupiaReportAction[] actionButtons = {
			new CreateTeamRoomAction(), new DeleteTeamRoomAction(), new EditTeamRoomAction()
	};

	/**
	 * Creates the account summary report and passes the account name, magic
	 * number and storage category to the implementing class
	 */
	public TeamRoomsReport() {
		super();
		// Use column 0 (the hidden column) as context
		this.setMgmtColumnIndex(0);
	}

	/**
	 * This method returns the report label to be display in UI
	 *
	 * @return label of report
	 */
	@Override
	public String getReportLabel() {
		return SparkConstants.TEAM_ROOMS_LABEL;
	}

	/**
	 * @return This method returns report name ,each report should have unique
	 *         name
	 */
	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	@Override
	public Class<TeamRoomsReportImpl> getImplementationClass() {
		return TeamRoomsReportImpl.class;
	}

	@Override
	public ContextMapRule[] getMapRules() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(SparkConstants.TEAM_LIST_DRILLDOWN);

		ContextMapRule rule = new ContextMapRule();
		rule.setContextName(context.getId());
		rule.setContextType(context.getType());

		ContextMapRule[] rules = new ContextMapRule[1];
		rules[0] = rule;

		return rules;
	}

	@Override
	public CloupiaReport[] getDrilldownReports() {
		return null;
	}

	@Override
	public CloupiaReportAction[] getActions() {
		return this.actionButtons;
	}

	@Override
	public boolean isEasyReport() {
		return false;
	}

	@Override
	public boolean isLeafReport() {
		return false;
	}

}
