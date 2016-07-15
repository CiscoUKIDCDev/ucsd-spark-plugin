/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.teams.membership;

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
 * This calls the Spark API to create the requested team membership.
 *
 * @author Matt Day
 * @see SparkApi#createMembership
 * @see AddTeamMembershipConfig
 *
 */
public class AddTeamMembershipTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		AddTeamMembershipConfig config = (AddTeamMembershipConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		// Attempt to create the membership
		SparkApiStatus s = SparkApi.createTeamMembership(account, config.getTeamId(), config.getEmail(),
				config.isModerator());

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to create team membership: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Added team member");
		// Get message ID for rollback:
		try {
			SparkMembership membership = SparkApi.getMembershipResponse(s.getJson());
			if (membership.getId() != null) {
				context.getChangeTracker().undoableResourceAdded("Membership", membership.getPersonEmail(),
						"Team Membership", "Undo adding team membership",
						SparkConstants.DELETE_TEAM_MEMBERSHIP_TASK_LABEL,
						new DeleteTeamMembershipConfig(config, membership.getPersonEmail()));
			}
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register outputs for task: " + e.getMessage());
		}

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new AddTeamMembershipConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.ADD_TEAM_MEMBERSHIP_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {};
		return ops;
	}
}
