/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

/**
 * Provides status messages on Spark API requests
 *
 * @author Matt Day
 *
 */
public class SparkAPIStatus {
	private boolean success;
	private String error;

	/**
	 * Initialise with error and success values
	 *
	 * @param success
	 *            if it's a success or not
	 * @param error
	 *            any error message (can be null)
	 */
	public SparkAPIStatus(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
	}

	/**
	 * @return false if the task was not successful
	 */
	public boolean isSuccess() {
		return this.success;
	}

	/**
	 * Any error messages are captured here.
	 *
	 * @return null or blank on no error, otherwise the error message
	 */
	public String getError() {
		return this.error;
	}

}
