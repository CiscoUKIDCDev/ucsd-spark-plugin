/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.teams.actions;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cisco.ukidcv.spark.reports.teams.SparkTeamReport;
import com.cisco.ukidcv.spark.tasks.teams.DeleteTeamConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button allowing the user to delete a new team
 *
 * @author Matt Day
 * @see SparkTeamReport
 * @see DeleteTeamConfig
 *
 */
public class DeleteTeamAction extends CloupiaPageAction {
	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.ukidcv.spark.reports.teams.actions.DeleteTeamForm";
	private static final String ACTION_ID = "com.cisco.ukidcv.spark.reports.teams.actions.DeleteTeamAction";
	private static final String LABEL = SparkConstants.DELETE_TEAM_TASK_LABEL;
	private static final String DESCRIPTION = SparkConstants.DELETE_TEAM_TASK_LABEL;

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Delete Host custom task
		page.bind(FORM_ID, DeleteTeamConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		DeleteTeamConfig form = new DeleteTeamConfig();

		// Pre-populate the team field:
		form.setTeamName(query);

		// Set the team field to read-only
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
		DeleteTeamConfig config = (DeleteTeamConfig) obj;

		SparkAccount account = new SparkAccount(context);

		SparkApiStatus s = SparkApi.deleteTeam(account, config.getTeamId());

		if (!s.isSuccess()) {
			page.setPageMessage("Could not delete team: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Team deleted OK");
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
