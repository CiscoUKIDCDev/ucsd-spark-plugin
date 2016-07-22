/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.membership;

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
 * This calls the Spark API to delete the requested membership.
 * <p>
 * It will look up the member via email address and room ID
 *
 * @author Matt Day
 * @see SparkApi#updateMembership
 * @see DeleteMembershipConfig
 *
 */
public class DeleteMembershipTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		DeleteMembershipConfig config = (DeleteMembershipConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		// First obtain the Membership ID
		String memberId = SparkApi.getSparkMemberships(account, config.getRoomId(), config.getEmail());
		if (memberId == null) {
			ucsdLogger.addError("Cannot find email: " + config.getEmail());
			throw new SparkTaskFailedException("Cannot find email: " + config.getEmail());
		}

		// Attempt to delete the membership
		SparkApiStatus s = SparkApi.deleteMembership(account, memberId);

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to delete membership: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Deleted member");

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new DeleteMembershipConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.DELETE_ROOM_MEMBERSHIP_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {};
		return ops;
	}
}
