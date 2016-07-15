/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.messages;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.api.json.SparkMessage;
import com.cisco.ukidcv.spark.api.json.SparkMessageResponse;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cisco.ukidcv.spark.inputs.SparkMessageSelector;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * This calls the Spark API to post a message to an email address
 *
 * @author Matt Day
 * @see SparkApi#createRoom
 * @see PostMessageToEmailConfig
 * @see SparkMessageSelector
 */
public class PostMessageToEmailTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		PostMessageToEmailConfig config = (PostMessageToEmailConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		// Construct Spark Message:
		SparkMessage message = new SparkMessage(config.getMessage());

		// Add file URL:
		if ((config.getFileUrl() != null) && (!"".equals(config.getFileUrl()))) {
			message.addFiles(config.getFileUrl());
		}

		// Post message
		SparkApiStatus s = SparkApi.sendMessageToPerson(account, config.getEmail(), message);

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to post message: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Posted Message");

		// Get message ID for rollback:
		try {
			SparkMessageResponse r = SparkApi.getMessageResponse(s.getJson());
			if (r.getId() != null) {
				// Format the message the same as the SparkMessageSelector
				final String messageId = r.getId();
				context.saveOutputValue(SparkConstants.MESSAGE_LIST_FORM_LABEL, messageId);

				context.getChangeTracker().undoableResourceAdded("Message", r.getId(), "Message Posted",
						"Undo message post", SparkConstants.DELETE_MESSAGE_TASK_LABEL,
						new DeleteMessageConfig(config, r.getId()));
			}
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register outputs for task: " + e.getMessage());
		}

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new PostMessageToEmailConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.POST_MESSAGE_EMAIL_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(SparkConstants.MESSAGE_LIST_FORM_LABEL, SparkConstants.GENERIC_TEXT_INPUT,
						SparkConstants.MESSAGE_LIST_FORM_LABEL),
		};
		return ops;
	}
}
