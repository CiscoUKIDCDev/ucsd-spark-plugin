/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.summary;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.reports.summary.graphs.SparkRoomTypesPieChart;
import com.cloupia.model.cIM.InfraAccountTypes;
import com.cloupia.service.cIM.inframgr.collector.impl.GenericInfraAccountReport;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;

/**
 * Builds the account overview page - this is the "Summary" tab with the graphs
 * on it.
 * <p>
 * Each graph is actually added as a drilldown report, but is rendered on this
 * page. Anything not in here will show up in the More Reports tab.
 *
 * @author Matt Day
 *
 */
public class AccountReport extends GenericInfraAccountReport {

	// Drilldown reports MUST be defined like this (i.e. not in the
	// getDrilldownReports method)
	private CloupiaReport[] drilldown = new CloupiaReport[] {
			new SummaryTable(), new SparkRoomTypesPieChart(),
	};

	/**
	 * Creates the account summary report and passes the account name, magic
	 * number and storage category to the implementing class
	 */
	public AccountReport() {
		super(SparkConstants.INFRA_ACCOUNT_LABEL, SparkConstants.INFRA_ACCOUNT_MAGIC_NUMBER,
				InfraAccountTypes.CAT_STORAGE);
	}

	@Override
	public CloupiaReport[] getDrilldownReports() {
		return this.drilldown;
	}

}
