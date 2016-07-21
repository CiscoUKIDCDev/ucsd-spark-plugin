/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.teams.membership;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task to delete a Spark membership. This task uses the email
 * address and team ID to remove a user as UCSD cannot cache every user in
 * spark.
 * <p>
 * The execution task will look up the membership ID to delete the member
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see DeleteTeamMembershipTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_delete_team_membership")
public class DeleteTeamMembershipConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.TEAM_NAME_LABEL, help = SparkConstants.TEAM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.TEAM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.TEAM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String teamName;

	@FormField(label = SparkConstants.EMAIL_LABEL, help = SparkConstants.EMAIL_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String email;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public DeleteTeamMembershipConfig() {
		super();

	}

	/**
	 * Rollback constructor. This is used from the AddMembershipTask to allow
	 * UCS Director to undo adding the member to a team
	 *
	 * @param config
	 *            Original configuration to post the message
	 * @param email
	 *            Message ID from posted message
	 *
	 * @see AddTeamMembershipTask
	 */
	public DeleteTeamMembershipConfig(AddTeamMembershipConfig config, String email) {
		final String teamId = config.getAccount() + ";" + config.getTeamId() + ";" + config.getTeamName();
		this.setTeamName(teamId);
		this.setEmail(email);
	}

	/**
	 * @return the selected account
	 */
	public String getAccount() {
		// We're only interested in the first part, remove anything after ;
		return this.teamName.split(";")[0];
	}

	/**
	 * Set the membership name
	 *
	 * @param email
	 *            Email address to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return The user provided email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Set the team name
	 *
	 * @param teamName
	 *            team name to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * @return Get selected team ID
	 */
	public String getTeamId() {
		// The team name will be the context ID in the format Account;teamId
		return this.teamName.split(";")[1];
	}

	@Override
	public long getActionId() {
		return this.actionId;
	}

	@Override
	public long getConfigEntryId() {
		return this.configEntryId;
	}

	@Override
	public String getDisplayLabel() {
		return SparkConstants.DELETE_TEAM_MEMBERSHIP_TASK_LABEL;
	}

	@Override
	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	@Override
	public void setConfigEntryId(long configEntryId) {
		this.configEntryId = configEntryId;
	}

}
