/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.rooms;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task to create a new Spark room
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see CreateRoomTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_create_room_collection")
public class CreateRoomConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ACCOUNT_LIST_FORM_LABEL, help = SparkConstants.ACCOUNT_LIST_FORM_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = SparkConstants.ROOM_NAME_LABEL, help = SparkConstants.ROOM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String roomName;

	@FormField(label = SparkConstants.TEAM_NAME_LABEL, help = SparkConstants.TEAM_NAME_LABEL, mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.TEAM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.TEAM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String teamName;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public CreateRoomConfig() {
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
	 * Set the room name
	 *
	 * @param roomName
	 *            room name to set
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/**
	 * @return The user provided room name
	 */
	public String getRoomName() {
		return this.roomName;
	}

	/**
	 * @return The user provided team name
	 */
	public String getTeamId() {
		// Return null if the team wasn't specified
		try {
			return this.teamName.split(";")[1];
		}
		catch (@SuppressWarnings("unused") ArrayIndexOutOfBoundsException e) {
			return null;
		}
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
		return SparkConstants.CREATE_ROOM_TASK_LABEL;
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
