/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.reports.teams.drilldowns.actions;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.api.json.SparkTeam;
import com.cisco.ukidcv.spark.api.json.SparkTeamMembership;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cisco.ukidcv.spark.tasks.membership.EditMembershipConfig;
import com.cisco.ukidcv.spark.tasks.teams.membership.EditTeamMembershipConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button allowing the user to edit an existing membership
 * <p>
 * This uses the EditMembership task to present the GUI, setting certain fields
 * read-only if they're known.
 *
 * @author Matt Day
 * @see EditMembershipConfig
 *
 */
public class EditTeamMembershipAction extends CloupiaPageAction {
	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.ukidcv.spark.reports.teams.drilldowns.actions.EditMembershipForm";
	private static final String ACTION_ID = "com.cisco.ukidcv.spark.reports.teams.drilldowns.actions.EditMembershipAction";
	private static final String LABEL = SparkConstants.EDIT_MEMBERSHIP_TASK_LABEL;
	private static final String DESCRIPTION = SparkConstants.EDIT_MEMBERSHIP_TASK_LABEL;

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Edit Host custom task
		page.bind(FORM_ID, EditTeamMembershipConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		EditTeamMembershipConfig form = new EditTeamMembershipConfig();
		SparkAccount account = new SparkAccount(context);

		// Get email address and member ID from context
		final String memberId = query.split(";")[1];
		final String email = query.split(";")[2];

		// Get team information from ID:
		SparkTeamMembership m = SparkApi.getSparkTeamDetails(account, memberId);

		if (m == null) {
			throw new SparkTaskFailedException("Could not find membership details");
		}

		// Look up Email address:
		SparkTeam r = SparkInventory.getTeam(account, m.getTeamId());

		// Create a string to match the SparkTeamSelector internal ID
		final String teamContextId = account.getAccountName() + ";" + m.getTeamId() + ";" + r.getName();

		// Pre-populate the email and teamId field (it has the same context as
		// the selection):
		form.setEmail(email);
		form.setTeamName(teamContextId);

		// Set the email and team name fields to read-only as this is an action
		// button
		page.getFlist().getByFieldId(FORM_ID + ".teamName").setEditable(false);
		page.getFlist().getByFieldId(FORM_ID + ".email").setEditable(false);

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
		EditTeamMembershipConfig config = (EditTeamMembershipConfig) obj;

		SparkAccount account = new SparkAccount(context);

		// First obtain the Membership ID
		String memberId = SparkApi.getSparkTeamMemberships(account, config.getTeamId(), config.getEmail());
		if (memberId == null) {
			throw new SparkTaskFailedException("Cannot find email: " + config.getEmail());
		}

		// Attempt to edit the membership
		SparkApiStatus s = SparkApi.updateTeamMembership(account, memberId, config.isModerator());

		if (!s.isSuccess()) {
			page.setPageMessage("Editing team member failed: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Team member edited OK");
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
