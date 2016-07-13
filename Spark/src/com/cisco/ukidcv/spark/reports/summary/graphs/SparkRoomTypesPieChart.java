/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.summary.graphs;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.DynReportContext;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.model.cIM.ReportDefinition;
import com.cloupia.service.cIM.inframgr.reportengine.ContextMapRule;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaNonTabularReport;

/**
 * Creates a pie chart list of values for the different Spark room types (direct
 * and group).
 * <p>
 * This is the descriptor class - it provides information and hints for UCS
 * Director, including the implementation class.
 *
 * @author Matt Day
 * @see SparkRoomTypesPieChartImpl
 *
 */
public class SparkRoomTypesPieChart extends CloupiaNonTabularReport {
	private static final String NAME = "com.cisco.ukidcv.spark.reports.summary.graphs.SparkRoomTypesPieChart";

	/**
	 * @return BarChartReport implementation class type
	 */
	@Override
	public Class<SparkRoomTypesPieChartImpl> getImplementationClass() {
		return SparkRoomTypesPieChartImpl.class;
	}

	/**
	 * Initialise with default values
	 */
	public SparkRoomTypesPieChart() {
		super();
	}

	// Returns report type for pie chart as shown below
	@Override
	public int getReportType() {
		return ReportDefinition.REPORT_TYPE_SNAPSHOT;
	}

	@Override
	public String getReportLabel() {
		return SparkConstants.ROOM_TYPE_LABEL;
	}

	@Override
	public boolean isLeafReport() {
		return false;
	}

	@Override
	public String getReportName() {
		return NAME;
	}

	// Forcing this report into the Physical->Storage part of the GUI.
	@Override
	public int getMenuID() {
		return 51;
	}

	/**
	 * @return true if you want this chart to show up in a summary report
	 */
	@Override
	public boolean showInSummary() {
		return true;
	}

	// Returns report hint for pie chart as shown below
	@Override
	public int getReportHint() {
		return ReportDefinition.REPORT_HINT_PIECHART;
	}

	// Context tells UCSD which report owns this one
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
