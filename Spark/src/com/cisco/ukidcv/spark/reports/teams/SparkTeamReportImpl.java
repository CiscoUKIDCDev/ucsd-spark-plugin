/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.teams;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.json.SparkRoom;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.model.cIM.TabularReport;
import com.cloupia.service.cIM.inframgr.TabularReportGeneratorIf;
import com.cloupia.service.cIM.inframgr.reportengine.ReportRegistryEntry;
import com.cloupia.service.cIM.inframgr.reports.TabularReportInternalModel;

/**
 * Implements a room report - a list of all rooms in the converged view.
 *
 * @author Matt Day
 * @see SparkTeamReport
 *
 */
public class SparkTeamReportImpl implements TabularReportGeneratorIf {

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
		model.addTextColumn("Type", "Type");
		model.addTextColumn("Locked", "Locked");
		model.addTextColumn("Last activity", "Last activity");
		model.addTextColumn("Created", "Created");
		model.completedHeader();

		SparkAccount account = new SparkAccount(context);

		for (SparkRoom room : SparkInventory.getRooms(account).getItems()) {
			// Generate internal ID in the format AccountName;RoomID;Room Name
			final String internalId = account.getAccountName() + ";" + room.getId() + ";" + room.getTitle();

			/*
			 * Now add the various attributes as per above (the number of
			 * entries must match the number of column headers or it will throw
			 * an exception)
			 */
			model.addTextValue(internalId);
			model.addTextValue(room.getTitle());
			model.addTextValue(room.getType());
			model.addTextValue(room.getIsLocked().toString());
			model.addTextValue(room.getLastActivity());
			model.addTextValue(room.getCreated());
			model.completedRow();
		}

		model.updateReport(report);

		return report;

	}

}
