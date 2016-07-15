/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.membership;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.api.json.SparkMembership;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * This calls the Spark API to create the requested membership.
 *
 * @author Matt Day
 * @see SparkApi#createMembership
 * @see AddMembershipConfig
 *
 */
public class AddMembershipTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		AddMembershipConfig config = (AddMembershipConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		// Attempt to create the membership
		SparkApiStatus s = SparkApi.createMembership(account, config.getRoomId(), config.getEmail(),
				config.isModerator());

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to create membership: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Added member");
		// Get message ID for rollback:
		try {
			SparkMembership membership = SparkApi.getMembershipResponse(s.getJson());
			if (membership.getId() != null) {
				context.getChangeTracker().undoableResourceAdded("Membership", membership.getPersonEmail(),
						"Membership", "Undo adding membership", SparkConstants.DELETE_MEMBERSHIP_TASK_LABEL,
						new DeleteMembershipConfig(config, membership.getPersonEmail()));
			}
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register outputs for task: " + e.getMessage());
		}

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new AddMembershipConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.ADD_MEMBERSHIP_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {};
		return ops;
	}
}
