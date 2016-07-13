/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.tasks.inventory;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.TaskConfigIf;
import com.cloupia.service.cIM.inframgr.customactions.UserInputField;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * Configuration task to collect inventory.
 * <p>
 * This provides the GUI and configuration elements to execute this task. It can
 * be used via an action button or as a workflow task.
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "spark_inventory_collection")
public class CollectInventoryConfig implements TaskConfigIf {

	@Persistent
	private long configEntryId;

	@Persistent
	private long actionId;

	@FormField(label = SparkConstants.ACCOUNT_LIST_FORM_LABEL, help = SparkConstants.ACCOUNT_LIST_FORM_LABEL, mandatory = true, type = FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP, table = SparkConstants.ACCOUNT_LIST_FORM_PROVIDER)
	@UserInputField(type = SparkConstants.ACCOUNT_LIST_FORM_TABLE_NAME)
	@Persistent
	private String account;

	/**
	 * Empty default constructor - you could initialise default values here if
	 * you wanted
	 */
	public CollectInventoryConfig() {

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
		return SparkConstants.INVENTORY_TASK_LABEL;
	}

	/**
	 * Get the account name
	 *
	 * @return Account name to do this on
	 */
	public String getAccount() {
		return this.account;
	}

	/**
	 * Set the account name
	 *
	 * @param account
	 *            The host to be created
	 */
	public void setAccount(String account) {
		this.account = account;
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
