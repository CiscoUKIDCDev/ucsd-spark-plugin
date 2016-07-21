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
 * Configuration task to edit a new Spark room
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see EditRoomTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_edit_room")
public class EditRoomConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ROOM_NAME_LABEL, help = SparkConstants.ROOM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ROOM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ROOM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String roomName;

	@FormField(label = SparkConstants.NEW_ROOM_NAME_LABEL, help = SparkConstants.NEW_ROOM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String newRoomName;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public EditRoomConfig() {
		super();
	}

	/**
	 * @return the selected account
	 */
	public String getAccount() {
		// We're only interested in the first part, remove anything after ;
		return this.roomName.split(";")[0];
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
	 * @return Get selected room ID
	 */
	public String getRoomId() {
		// The room name will be the context ID in the format Account;roomId
		return this.roomName.split(";")[1];
	}

	/**
	 * @return new room name
	 */
	public String getNewRoomName() {
		return this.newRoomName;
	}

	/**
	 * Set new room name
	 *
	 * @param newRoomName
	 *            name for the new room
	 */
	public void setNewRoomName(String newRoomName) {
		this.newRoomName = newRoomName;
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
		return SparkConstants.EDIT_ROOM_TASK_LABEL;
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
