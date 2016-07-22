/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
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
 * Configuration task to delete a new Spark team
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see DeleteTeamTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_delete_team_collection")
public class DeleteTeamConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.TEAM_NAME_LABEL, help = SparkConstants.TEAM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.TEAM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.TEAM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String teamName;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public DeleteTeamConfig() {
		super();
	}

	/**
	 * Rollback constructor. This is used from the CreateTeamTask to allow UCS
	 * Director to undo the team creation.
	 *
	 * @param config
	 *            Original configuration to create the team
	 * @param teamId
	 *            Team ID from created team
	 *
	 * @see CreateTeamTask
	 */
	public DeleteTeamConfig(CreateTeamConfig config, String teamId) {
		// Set the team name in the same format as UCS Director
		this.setTeamName(config.getAccount() + ";" + teamId + ";" + config.getTeamName());
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
	 * @return The team ID
	 */
	public String getTeamId() {
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
		return SparkConstants.DELETE_TEAM_TASK_LABEL;
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
