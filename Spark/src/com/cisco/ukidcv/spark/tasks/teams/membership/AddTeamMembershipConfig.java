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
 * Configuration task to create a new Spark team membership
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see AddTeamMembershipTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_create_team_membership")
public class AddTeamMembershipConfig implements TaskConfigIf {

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

	@FormField(label = SparkConstants.MODERATOR_LABEL, help = SparkConstants.MODERATOR_LABEL, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	@UserInputField(type = SparkConstants.BOOLEAN_INPUT)
	@Persistent
	private boolean moderator;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public AddTeamMembershipConfig() {
		super();

	}

	/**
	 * @return the selected account
	 */
	public String getAccount() {
		// We're only interested in the first part, remove anything after ;
		return this.teamName.split(";")[0];
	}

	/**
	 * Set the teamTeamMembership name
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
		// The team name will be the context ID in the format
		// Account;teamId;teamName
		return this.teamName.split(";")[1];
	}

	/**
	 * @return Get selected team Name
	 */
	public String getTeamName() {
		// The team name will be the context ID in the format
		// Account;teamId;teamName
		return this.teamName.split(";")[2];
	}

	/**
	 * Should this user be a moderator
	 *
	 * @return moderator status
	 */
	public boolean isModerator() {
		return this.moderator;
	}

	/**
	 * Set if this user should be a moderator
	 *
	 * @param moderator
	 *            true if they should be
	 */
	public void setModerator(boolean moderator) {
		this.moderator = moderator;
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
		return SparkConstants.ADD_TEAM_MEMBERSHIP_TASK_LABEL;
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
