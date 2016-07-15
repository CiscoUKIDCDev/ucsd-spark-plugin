/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.teams;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task to edit a new Spark team
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see EditTeamTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_edit_team")
public class EditTeamConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.TEAM_NAME_LABEL, help = SparkConstants.TEAM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.TEAM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.TEAM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String teamName;

	@FormField(label = SparkConstants.NEW_TEAM_NAME_LABEL, help = SparkConstants.NEW_TEAM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String newTeamName;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public EditTeamConfig() {
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

	/**
	 * @return new team name
	 */
	public String getNewTeamName() {
		return this.newTeamName;
	}

	/**
	 * Set new team name
	 *
	 * @param newTeamName
	 *            name for the new team
	 */
	public void setNewTeamName(String newTeamName) {
		this.newTeamName = newTeamName;
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
		return SparkConstants.EDIT_TEAM_TASK_LABEL;
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
