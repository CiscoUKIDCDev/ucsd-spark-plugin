/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.summary.graphs;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.json.SparkRoom;
import com.cisco.ukidcv.spark.api.json.SparkRooms;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.ReportNameValuePair;
import com.cloupia.model.cIM.SnapshotReport;
import com.cloupia.model.cIM.SnapshotReportCategory;
import com.cloupia.service.cIM.inframgr.SnapshotReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;

/**
 * Creates a pie chart list of values for the different Spark room types (direct
 * and group)
 * <p>
 * This is the implementation class.
 *
 * @author Matt Day
 * @see SparkRoomTypesPieChart
 *
 */
public class SparkRoomTypesPieChartImpl implements SnapshotReportGeneratorIf {

	@Override
	public SnapshotReport getSnapshotReport(ReportRegistryEntry reportEntry, ReportContext context) throws Exception {

		SnapshotReport report = new SnapshotReport();
		report.setContext(context);

		report.setReportName(reportEntry.getReportLabel());

		report.setNumericalData(true);

		report.setDisplayAsPie(true);

		report.setPrecision(0);

		final SparkAccount account = new SparkAccount(context);

		int group = 0;
		int direct = 0;
		SparkRooms rooms = SparkInventory.getRooms(account);
		for (SparkRoom room : rooms.getItems()) {
			if ("group".equals(room.getType())) {
				group++;
			}
			else if ("direct".equals(room.getType())) {
				direct++;
			}
		}

		ReportNameValuePair[] rnv = new ReportNameValuePair[2];
		rnv[0] = new ReportNameValuePair("Group", group);
		rnv[1] = new ReportNameValuePair("Direct", direct);

		SnapshotReportCategory cat = new SnapshotReportCategory();

		cat.setCategoryName(SparkConstants.ROOM_TYPE_LABEL);
		cat.setNameValuePairs(rnv);

		report.setCategories(new SnapshotReportCategory[] {
				cat
		});

		return report;
	}
}
