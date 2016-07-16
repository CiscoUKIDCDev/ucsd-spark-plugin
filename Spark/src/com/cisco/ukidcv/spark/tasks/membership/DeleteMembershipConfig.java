/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.membership;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task to delete a Spark membership. This task uses the email
 * address and room ID to remove a user as UCSD cannot cache every user in
 * spark.
 * <p>
 * The execution task will look up the membership ID to delete the member
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see DeleteMembershipTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_delete_membership")
public class DeleteMembershipConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ROOM_NAME_LABEL, help = SparkConstants.ROOM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ROOM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ROOM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String roomName;

	@FormField(label = SparkConstants.EMAIL_LABEL, help = SparkConstants.EMAIL_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String email;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public DeleteMembershipConfig() {
		super();

	}

	/**
	 * Rollback constructor. This is used from the AddMembershipTask to allow
	 * UCS Director to undo adding the member to a room
	 *
	 * @param config
	 *            Original configuration to post the message
	 * @param email
	 *            Message ID from posted message
	 *
	 * @see AddMembershipTask
	 */
	public DeleteMembershipConfig(AddMembershipConfig config, String email) {
		final String roomId = config.getAccount() + ";" + config.getRoomId() + ";" + config.getRoomName();
		this.setRoomName(roomId);
		this.setEmail(email);
	}

	/**
	 * @return the selected account
	 */
	public String getAccount() {
		// We're only interested in the first part, remove anything after ;
		return this.roomName.split(";")[0];
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
		return SparkConstants.DELETE_ROOM_MEMBERSHIP_TASK_LABEL;
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
