/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
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
 * Configuration task to delete a message from a Spark room
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 * @see PostMessageTask
 */
@PersistenceCapable(detachable = "true", table = "Spark_delete_message_from_room")
public class DeleteMessageConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ACCOUNT_LIST_FORM_LABEL, help = SparkConstants.ACCOUNT_LIST_FORM_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	@FormField(label = SparkConstants.MESSAGE_LIST_FORM_LABEL, help = SparkConstants.MESSAGE_LIST_FORM_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TEXT)
	@UserInputField(type = SparkConstants.GENERIC_TEXT_INPUT)
	@Persistent
	private String messageId;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public DeleteMessageConfig() {
		super();
	}

	/**
	 * Rollback constructor. This is used from the PostMessageTask to allow UCS
	 * Director to undo the message posted.
	 *
	 * @param config
	 *            Original configuration to post the message
	 * @param messageId
	 *            Message ID from posted message
	 *
	 * @see PostMessageTask
	 */
	public DeleteMessageConfig(PostMessageConfig config, String messageId) {
		this.setAccount(config.getAccount());
		this.setMessageId(messageId);
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
	 * @return Message ID set by the user
	 */
	public String getMessageId() {
		return this.messageId;
	}

	/**
	 * Set the message ID
	 *
	 * @param messageId
	 *            message ID to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
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
		return SparkConstants.DELETE_MESSAGE_TASK_LABEL;
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
