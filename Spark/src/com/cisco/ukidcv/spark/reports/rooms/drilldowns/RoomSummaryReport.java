/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.rooms.drilldowns;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

/**
 * Room summary drill down report.
 * <p>
 * Like any other summary report it shows an overview of a particular room
 * including graphs and so on
 * 
 * @author Matt Day
 *
 */
public class RoomSummaryReport extends CloupiaNonTabularReport {

	private final static String REPORT_NAME = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.RoomSummaryReport";

	/**
	 * Creates the account summary report and passes the account name, magic
	 * number and storage category to the implementing class
	 */
	public RoomSummaryReport() {
		super();
		this.setMgmtColumnIndex(1);
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
	 * @return This method returns report name ,each report should have unique
	 *         name
	 */
	@Override
	public String getReportName() {
		return REPORT_NAME;
	}

	@Override
	public Class<RoomSummaryReportImpl> getImplementationClass() {
		return RoomSummaryReportImpl.class;
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
	 * @return This report returns boolean value true/false. Returns true if it
	 *         is leaf report otherwise it returns false
	 */
	@Override
	public boolean isLeafReport() {
		return false;
	}

	/**
	 * @return This method returns boolean value true/false. if it returns true
	 *         then only form will be shown in UI
	 */
	@Override
	public boolean isManagementReport() {
		return true;
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

}
