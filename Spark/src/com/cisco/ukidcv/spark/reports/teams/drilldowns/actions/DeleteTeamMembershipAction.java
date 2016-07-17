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
import com.cisco.ukidcv.spark.tasks.teams.membership.DeleteTeamMembershipConfig;
import com.cloupia.model.cIM.ConfigTableAction;
import com.cloupia.model.cIM.ReportContext;
import com.cloupia.service.cIM.inframgr.forms.wizard.Page;
import com.cloupia.service.cIM.inframgr.forms.wizard.PageIf;
import com.cloupia.service.cIM.inframgr.forms.wizard.WizardSession;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaPageAction;

/**
 * Action button allowing the user to delete an existing team membership
 * <p>
 * *
 * <p>
 * This uses the DeleteMembership task to present the GUI, setting certain
 * fields read-only if they're known.
 *
 * @author Matt Day
 * @see DeleteTeamMembershipConfig
 *
 */
public class DeleteTeamMembershipAction extends CloupiaPageAction {
	// need to provide a unique string to identify this form and action
	private static final String FORM_ID = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.DeleteTeamMembershipForm";
	private static final String ACTION_ID = "com.cisco.ukidcv.spark.reports.rooms.drilldowns.actions.DeleteTeamMembershipAction";
	private static final String LABEL = SparkConstants.DELETE_MEMBERSHIP_TASK_LABEL;
	private static final String DESCRIPTION = SparkConstants.DELETE_MEMBERSHIP_TASK_LABEL;

	@Override
	public void definePage(Page page, ReportContext context) {
		// Use the same form (config) as the Delete Host custom task
		page.bind(FORM_ID, DeleteTeamMembershipConfig.class);
	}

	/**
	 * This sets up the initial fields and provides default values (in this case
	 * the account name)
	 */
	@Override
	public void loadDataToPage(Page page, ReportContext context, WizardSession session) throws Exception {
		String query = context.getId();
		DeleteTeamMembershipConfig form = new DeleteTeamMembershipConfig();
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
		final String roomContextId = account.getAccountName() + ";" + m.getTeamId() + ";" + r.getName();

		// Pre-populate the email and roomId field (it has the same context as
		// the selection):
		form.setEmail(email);
		form.setTeamName(roomContextId);

		// Set the email and room name fields to read-only as this is an action
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
		DeleteTeamMembershipConfig config = (DeleteTeamMembershipConfig) obj;

		SparkAccount account = new SparkAccount(context);

		// First obtain the Membership ID
		String memberId = SparkApi.getSparkTeamMemberships(account, config.getTeamId(), config.getEmail());
		if (memberId == null) {
			throw new SparkTaskFailedException("Cannot find email: " + config.getEmail());
		}

		// Attempt to delete the membership
		SparkApiStatus s = SparkApi.deleteTeamMembership(account, memberId);

		if (!s.isSuccess()) {
			page.setPageMessage("Could not remove team member: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}

		// Set the text for the "OK" prompt and return successfully
		page.setPageMessage("Member removed OK");
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
