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
 * Configuration task to create a new Spark team
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see CreateTeamTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_create_team_collection")
public class CreateTeamConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ACCOUNT_LIST_FORM_LABEL, help = SparkConstants.ACCOUNT_LIST_FORM_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = SparkConstants.TEAM_NAME_LABEL, help = SparkConstants.TEAM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String teamName;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public CreateTeamConfig() {
		super();

	}

	/**
	 * @return the selected account
	 */
	public String getAccount() {
		// We're only interested in the first part, remove anything after ;
		return this.account.split(";")[0];
	}

	/**
	 * Set the account
	 *
	 * @param account
	 *            Account to set
	 */
	public void setAccount(String account) {
		this.account = account;
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
	 * @return The user provided team name
	 */
	public String getTeamName() {
		return this.teamName;
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
		return SparkConstants.CREATE_TEAM_TASK_LABEL;
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
