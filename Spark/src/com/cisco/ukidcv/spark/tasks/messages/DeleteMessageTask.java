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
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * This calls the Spark API to delete the requested room.
 *
 * @author Matt Day
 * @see SparkApi#deleteRoom
 * @see DeleteMessageConfig
 */
public class DeleteMessageTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		DeleteMessageConfig config = (DeleteMessageConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		// Delete message
		SparkApiStatus s = SparkApi.deleteMessage(account, config.getMessageId());

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to delete message: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Deleted Message");
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new DeleteMessageConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.DELETE_MESSAGE_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {};
		return ops;
	}
}
