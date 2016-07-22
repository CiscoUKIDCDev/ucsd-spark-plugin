/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.api.json.SparkMessage;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cisco.ukidcv.spark.tasks.messages.PostMessageToRoomConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button to post a new message to a room
 * <p>
 * This uses the PostMessage task to present the GUI, setting certain fields
 * read-only if they're known.
 *
 * @author Matt Day
 * @see PostMessageToRoomConfig
 *
 */
public class PostMessageAction extends CloupiaPageAction {
	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.PostMessageForm";
	private static final String ACTION_ID = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.PostMessageAction";
	private static final String LABEL = SparkConstants.POST_MESSAGE_TASK_LABEL;
	private static final String DESCRIPTION = SparkConstants.POST_MESSAGE_TASK_LABEL;

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Host custom task
		page.bind(FORM_ID, PostMessageToRoomConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		PostMessageToRoomConfig form = new PostMessageToRoomConfig();

		// Pre-populate the account field:
		form.setRoomName(query);

		// Set the account field to read-only
		page.getFlist().getByFieldId(FORM_ID + ".roomName").setEditable(false);

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
		PostMessageToRoomConfig config = (PostMessageToRoomConfig) obj;

		SparkAccount account = new SparkAccount(context);

		// Construct Spark Message:
		SparkMessage message = new SparkMessage();

		// Add message
		if ((config.getMessage() != null) && (!"".equals(config.getMessage()))) {
			message.setMarkdown(config.getMessage());
		}

		// Add file URL:
		if ((config.getFileUrl() != null) && (!"".equals(config.getFileUrl()))) {
			message.addFiles(config.getFileUrl());
		}

		// Add markdown
		if ((config.getMarkdown() != null) && (!"".equals(config.getMarkdown()))) {
			message.setMarkdown(config.getMarkdown());
		}

		// Post message
		SparkApiStatus s = SparkApi.sendMessageToRoom(account, config.getRoomId(), message);

		if (!s.isSuccess()) {
			page.setPageMessage("Could not post message to room: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Message posted OK");
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
