/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cisco.ukidcv.spark.tasks.messages.DeleteMessageConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button to delete a new message to a room
 * <p>
 * This uses the DeleteMessage task to present the GUI, setting certain fields
 * read-only if they're known.
 *
 * @author Matt Day
 * @see DeleteMessageConfig
 *
 */
public class DeleteMessageAction extends CloupiaPageAction {
	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.DeleteMessageForm";
	private static final String ACTION_ID = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.DeleteMessageAction";
	private static final String LABEL = SparkConstants.DELETE_MESSAGE_TASK_LABEL;
	private static final String DESCRIPTION = SparkConstants.DELETE_MESSAGE_TASK_LABEL;

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Create Host custom task
		page.bind(FORM_ID, DeleteMessageConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		DeleteMessageConfig form = new DeleteMessageConfig();

		// Pre-populate the account field:
		form.setAccount(query.split(";")[0]);

		form.setMessageId(query.split(";")[2]);

		// Set the account field to read-only
		page.getFlist().getByFieldId(FORM_ID + ".account").setEditable(false);
		// Set the message ID field to read-only
		page.getFlist().getByFieldId(FORM_ID + ".messageId").setEditable(false);

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
		DeleteMessageConfig config = (DeleteMessageConfig) obj;

		SparkAccount account = new SparkAccount(context);

		// Delete message
		SparkApiStatus s = SparkApi.deleteMessage(account, config.getMessageId());

		if (!s.isSuccess()) {
			page.setPageMessage("Could not delete message: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Message deleted OK");
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
		return true;
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
