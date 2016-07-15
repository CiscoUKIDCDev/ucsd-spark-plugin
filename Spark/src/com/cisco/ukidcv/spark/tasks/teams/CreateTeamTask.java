/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.teams;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.api.json.SparkTeam;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * This calls the Spark API to create the requested team.
 *
 * @author Matt Day
 * @see SparkApi#createTeam
 * @see CreateTeamConfig
 *
 */
public class CreateTeamTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		CreateTeamConfig config = (CreateTeamConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		// Attempt to create the team
		SparkApiStatus s = SparkApi.createTeam(account, config.getTeamName());

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to create team: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Created team: " + config.getTeamName());

		// Save team ID as an output for other tasks and register rollback task
		try {
			SparkTeam team = SparkApi.getTeamResponse(s.getJson());
			if (team.getId() != null) {
				// Format the message the same as the SparkTeamSelector
				final String internalId = account.getAccountName() + ";" + team.getId() + ";" + team.getName();
				context.saveOutputValue(SparkConstants.TEAM_NAME_LABEL, internalId);

				context.getChangeTracker().undoableResourceAdded("Team", team.getId(), "Team created",
						"Undo creation of team: " + config.getTeamName(), SparkConstants.DELETE_TEAM_TASK_LABEL,
						new DeleteTeamConfig(config, team.getId()));

			}
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register outputs for task: " + e.getMessage());
		}
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new CreateTeamConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.CREATE_TEAM_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(SparkConstants.TEAM_NAME_LABEL, SparkConstants.TEAM_LIST_FORM_TABLE_NAME,
						SparkConstants.TEAM_NAME_LABEL),
		};
		return ops;
	}
}
