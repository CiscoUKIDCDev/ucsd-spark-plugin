/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.teams.drilldowns.actions;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cisco.ukidcv.spark.reports.rooms.SparkRoomReport;
import com.cisco.ukidcv.spark.tasks.rooms.CreateRoomConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button allowing the user to create a new room
 *
 * @author Matt Day
 * @see SparkRoomReport
 * @see CreateRoomConfig
 *
 */
public class CreateTeamRoomAction extends CloupiaPageAction {
	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.ukidcv.spark.reports.teams.drilldowns.actions.CreateRoomForm";
	private static final String ACTION_ID = "com.cisco.ukidcv.spark.reports.teams.drilldowns.actions.CreateRoomAction";
	private static final String LABEL = SparkConstants.CREATE_ROOM_TASK_LABEL;
	private static final String DESCRIPTION = SparkConstants.CREATE_ROOM_TASK_LABEL;

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Host custom task
		page.bind(FORM_ID, CreateRoomConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		CreateRoomConfig form = new CreateRoomConfig();

		// The form will be in the format Account;Pod - grab the former:
		final String accountName = query.split(";")[0];
		// Build team ID query
		final String teamId = accountName + ";" + query.split(";")[1] + ";" + query.split(";")[2];

		// Pre-populate the account and team field:
		form.setAccount(accountName);
		form.setTeamName(teamId);

		// Set the account and team fields to read-only
		page.getFlist().getByFieldId(FORM_ID + ".account").setEditable(false);
		page.getFlist().getByFieldId(FORM_ID + ".teamName").setEditable(false);

		session.getSessionAttributes().put(FORM_ID, form);
		page.marshallFromSession(FORM_ID);

	}

	/**
	 * This should do basic error checking (UCSD will enforce mandatory fields)
	 * and attempt to execute the task.
	 *
	 * Throwing an exception here will show the message as an error dialogue.
	 */
	@Override
	public int validatePageData(Page page, ReportContext context, WizardSession session) throws Exception {
		// Get credentials from the current context
		Object obj = page.unmarshallToSession(FORM_ID);
		CreateRoomConfig config = (CreateRoomConfig) obj;

		SparkAccount account = new SparkAccount(context);

		SparkApiStatus s;

		// Was a team specified? If so create the room for that team
		if ((config.getTeamId() != null) && (!"".equals(config.getTeamId()))) {
			s = SparkApi.createRoom(account, config.getRoomName(), config.getTeamId());
		}
		else {
			s = SparkApi.createRoom(account, config.getRoomName());
		}

		if (!s.isSuccess()) {
			page.setPageMessage("Could not create room: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Room created OK");
		return PageIf.STATUS_OK;
	}

	@Override
	public boolean isDoubleClickAction() {
		return false;
	}

	@Override
	public boolean isDrilldownAction() {
		return false;
	}

	@Override
	public boolean isMultiPageAction() {
		return false;
	}

	@Override
	public boolean isSelectionRequired() {
		return false;
	}

	@Override
	public String getActionId() {
		return ACTION_ID;
	}

	@Override
	public int getActionType() {
		return ConfigTableAction.ACTION_TYPE_POPUP_FORM;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public String getFormId() {
		return FORM_ID;
	}

	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String getTitle() {
		return LABEL;
	}

}
