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
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * This calls the Spark API to delete the requested team.
 *
 * @author Matt Day
 * @see SparkApi#deleteTeam
 * @see DeleteTeamConfig
 *
 */
public class DeleteTeamTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		DeleteTeamConfig config = (DeleteTeamConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		// Attempt to delete the team
		SparkApiStatus s = SparkApi.deleteTeam(account, config.getTeamId());

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to delete team: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Deleted team: " + config.getTeamId());

	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new DeleteTeamConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.DELETE_TEAM_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {};
		return ops;
	}
}
