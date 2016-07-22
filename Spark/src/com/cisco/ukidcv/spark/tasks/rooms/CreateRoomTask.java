/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.rooms;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.SparkApi;
import com.cisco.ukidcv.spark.api.SparkApiStatus;
import com.cisco.ukidcv.spark.api.json.SparkRoom;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkTaskFailedException;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * This calls the Spark API to create the requested room.
 *
 * @author Matt Day
 * @see SparkApi#createRoom
 * @see CreateRoomConfig
 *
 */
public class CreateRoomTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		CreateRoomConfig config = (CreateRoomConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());

		SparkApiStatus s;

		// Was a team specified? If so create the room for that team
		if ((config.getTeamId() != null) && (!"".equals(config.getTeamId()))) {
			s = SparkApi.createRoom(account, config.getRoomName(), config.getTeamId());
		}
		else {
			s = SparkApi.createRoom(account, config.getRoomName());
		}

		// If there was an error, log it and throw an exception
		if (!s.isSuccess()) {
			ucsdLogger.addError("Failed to create room: " + s.getError());
			throw new SparkTaskFailedException(s.getError());
		}
		ucsdLogger.addInfo("Created room: " + config.getRoomName());

		// Save room ID as an output for other tasks and register rollback task
		try {
			SparkRoom room = SparkApi.getRoomResponse(s.getJson());
			if (room.getId() != null) {
				// Format the message the same as the SparkRoomSelector
				final String internalId = account.getAccountName() + ";" + room.getId() + ";" + room.getTitle();
				context.saveOutputValue(SparkConstants.ROOM_NAME_LABEL, internalId);

				context.getChangeTracker().undoableResourceAdded("Room", room.getId(), "Room created",
						"Undo creation of room: " + config.getRoomName(), SparkConstants.DELETE_ROOM_TASK_LABEL,
						new DeleteRoomConfig(config, room.getId()));

			}
		}
		catch (Exception e) {
			ucsdLogger.addWarning("Could not register outputs for task: " + e.getMessage());
		}
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new CreateRoomConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.CREATE_ROOM_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {
				// Register output type for the volume created
				new TaskOutputDefinition(SparkConstants.ROOM_NAME_LABEL, SparkConstants.ROOM_LIST_FORM_TABLE_NAME,
						SparkConstants.ROOM_NAME_LABEL),
		};
		return ops;
	}
}
