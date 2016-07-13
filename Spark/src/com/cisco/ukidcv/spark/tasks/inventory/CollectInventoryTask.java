/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.inventory;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.TaskOutputDefinition;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionLogger;
import com.cloupia.service.cIM.inframgr.customactions.CustomActionTriggerContext;

/**
 * Performs the collection task
 * 
 * @author Matt Day
 *
 */
public class CollectInventoryTask extends AbstractTask {

	/**
	 * Executes the task
	 */
	@Override
	public void executeCustomAction(CustomActionTriggerContext context, CustomActionLogger ucsdLogger)
			throws Exception {
		CollectInventoryConfig config = (CollectInventoryConfig) context.loadConfigObject();
		SparkAccount account = new SparkAccount(config.getAccount());
		SparkInventory.update(account, SparkConstants.INVENTORY_REASON_USER, true);
		ucsdLogger.addInfo("Polled Inventory");
	}

	@Override
	public TaskConfigIf getTaskConfigImplementation() {
		return new CollectInventoryConfig();
	}

	@Override
	public String getTaskName() {
		return SparkConstants.INVENTORY_TASK_LABEL;
	}

	@Override
	public TaskOutputDefinition[] getTaskOutputDefinitions() {
		TaskOutputDefinition[] ops = {};
		return ops;
	}
}
