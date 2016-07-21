/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.rooms.drilldowns;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.DeleteMessageAction;
import com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.PostMessageAction;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReportAction;
import com.cloupia.service.cIM.inframgr.reports.simplified.DrillableReportWithActions;

/**
 * Room messages drill down report. This shows the recent messages.
 * <p>
 * Like any other summary report it shows an overview of a particular room
 * including graphs and so on
 *
 * @author Matt Day
 * @see RoomMessagesReportImpl
 *
 */
public class RoomMessagesReport extends DrillableReportWithActions {

	private final static String REPORT_NAME = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.RoomMessagesReport";

	private CloupiaReportAction[] actionButtons = {
			new PostMessageAction(), new DeleteMessageAction()
	};

	/**
	 * Creates the account summary report and passes the account name, magic
	 * number and storage category to the implementing class
	 */
	public RoomMessagesReport() {
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
		return SparkConstants.ROOM_RECENT_MESSAGES_LABEL;
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
	public Class<RoomMessagesReportImpl> getImplementationClass() {
		return RoomMessagesReportImpl.class;
	}

	@Override
	public ContextMapRule[] getMapRules() {
		DynReportContext context = ReportContextRegistry.getInstance()
				.getContextByName(SparkConstants.ROOM_LIST_DRILLDOWN);

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
