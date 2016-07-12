/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.constants;

/**
 * Stores various constants used throughout the code
 *
 * @author Matt Day
 *
 */
public class SparkConstants {

	// ======== UCSD Account Constants
	/**
	 * UCSD Internal account type. Should NOT be changed between releases or it
	 * breaks all kinds of things
	 */
	public static final String INFRA_ACCOUNT_TYPE = "SPARK";
	/**
	 * User-friendly label for this account type. This can be changed between
	 * releases.
	 */
	public static final String INFRA_ACCOUNT_LABEL = "Spark";
	/**
	 * Accounts must have a magic number in the converged view. The docs say to
	 * use something "over 1000". Let's hope no one else uses this value!
	 */
	public static final int INFRA_ACCOUNT_MAGIC_NUMBER = 19842701;
	/**
	 * Category to put all the workflows
	 */
	public static final String WORKFLOW_CATEGORY = "Spark";
	/**
	 * Folder to put the tasks in
	 */
	public static final String TASK_PREFIX = "Spark Tasks";
	// ======== Spark API Constants
	/**
	 * API Version
	 */
	public static final String API_VERSION = "2.5";
	/**
	 * Spark report server
	 */
	public static final String SPARK_SERVER = "api.opensparkmap.org";

	/**
	 * URI for spark report
	 */
	public final static String SPARK_REPORT_URI = "/data/2.5/spark";

}
