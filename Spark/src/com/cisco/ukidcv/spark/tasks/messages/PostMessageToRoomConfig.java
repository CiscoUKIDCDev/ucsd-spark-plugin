/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.messages;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task to post a message to a Spark room
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see PostMessageToRoomTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_post_message_to_room")
public class PostMessageToRoomConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ROOM_NAME_LABEL, help = SparkConstants.ROOM_NAME_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ROOM_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ROOM_LIST_FORM_TABLE_NAME)
	@Persistent
	private String roomName;

	@FormField(label = SparkConstants.MESSAGE_TEXT_LABEL, help = SparkConstants.MESSAGE_TEXT_LABEL, mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String message;

	@FormField(label = SparkConstants.MESSAGE_MARKDOWN_LABEL, help = SparkConstants.MESSAGE_MARKDOWN_LABEL, mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String markdown;

	@FormField(label = SparkConstants.MESSAGE_FILE_URL_LABEL, help = SparkConstants.MESSAGE_FILE_URL_LABEL, mandatory = false, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String fileUrl;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public PostMessageToRoomConfig() {
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
	 * @return user provided message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * Set the message to send
	 *
	 * @param message
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return user provided markdown
	 */
	public String getMarkdown() {
		return this.markdown;
	}

	/**
	 * Set the message to send
	 *
	 * @param message
	 */
	public void setMarkdown(String message) {
		this.message = this.markdown;
	}

	/**
	 * @return URL for any files to be sent
	 */
	public String getFileUrl() {
		return this.fileUrl;
	}

	/**
	 * Set a URL for a file to send
	 *
	 * @param fileUrl
	 *            URL for file
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	/**
	 * @return The user provided room name
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
		return SparkConstants.POST_MESSAGE_TASK_LABEL;
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
