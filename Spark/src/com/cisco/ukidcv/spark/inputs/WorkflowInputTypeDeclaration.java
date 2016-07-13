/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.inputs;

import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputFieldTypeDeclaration;
import com.cloupia.service.cIM.inframgr.customactions.WorkflowInputTypeRegistry;
import com.cloupia.service.cIM.inframgr.forms.wizard.TabularFieldRegistry;

/**
 * This static method registers all workflow inputs with UCS Director and
 * intialises list of values for workflows
 *
 * @author Matt Day
 *
 */
public class WorkflowInputTypeDeclaration {

	/**
	 * Private constructor - only want to access this statically
	 */
	private WorkflowInputTypeDeclaration() {
	}

	/**
	 * This method is used to register Workflow Input Types.
	 *
	 */
	public static void registerWFInputs() {
		registerAccountPicker();
	}

	/**
	 * Register the list of accounts
	 *
	 */
	private static void registerAccountPicker() {
		WorkflowInputTypeRegistry input = WorkflowInputTypeRegistry.getInstance();
		input.addDeclaration(new WorkflowInputFieldTypeDeclaration(SparkConstants.ACCOUNT_LIST_FORM_TABLE_NAME,
				SparkConstants.ACCOUNT_LIST_FORM_LABEL, FormFieldDefinition.FIELD_TYPE_TABULAR_POPUP,
				SparkConstants.ACCOUNT_LIST_FORM_NAME));

		// First item is what we return to the workflow, second is what we
		// display in the GUI
		TabularFieldRegistry.getInstance().registerTabularField(SparkConstants.ACCOUNT_LIST_FORM_NAME,
				SparkConstants.class, "0", "0");
	}

}
