/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
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
 * Configuration task to delete a new Spark room
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see DeleteRoomTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_delete_room_collection")
public class DeleteRoomConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ROOM_NAME_LABEL, help = SparkConstants.ROOM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ROOM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ROOM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String roomName;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public DeleteRoomConfig() {
		super();
	}

	/**
	 * Rollback constructor. This is used from the CreateRoomTask to allow UCS
	 * Director to undo the room creation.
	 * 
	 * @param config
	 *            Original configuration to create the room
	 * @param roomId
	 *            Room ID from created room
	 *
	 * @see CreateRoomTask
	 */
	public DeleteRoomConfig(CreateRoomConfig config, String roomId) {
		// Set the room name in the same format as UCS Director
		this.setRoomName(config.getAccount() + ";" + roomId + ";" + config.getRoomName());
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
	 * @return The room ID
	 */
	public String getRoomId() {
		return this.roomName.split(";")[1];
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
		return SparkConstants.DELETE_ROOM_TASK_LABEL;
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
