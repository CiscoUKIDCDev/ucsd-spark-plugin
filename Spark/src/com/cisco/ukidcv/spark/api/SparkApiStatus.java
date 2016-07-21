/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
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
public class SparkApiStatus {
	private final boolean success;
	private final String error;
	private final String json;

	/**
	 * Initialise with error and success values
	 *
	 * @param success
	 *            if it's a success or not
	 * @param error
	 *            any error message (can be null)
	 */
	public SparkApiStatus(boolean success, String error) {
		super();
		this.success = success;
		this.error = error;
		this.json = null;
	}

	/**
	 * Initialise with error and success values and the original JSON
	 *
	 * @param success
	 *            if it's a success or not
	 * @param error
	 *            any error message (can be null)
	 * @param json
	 *            Raw JSON from request
	 */
	public SparkApiStatus(boolean success, String error, String json) {
		super();
		this.success = success;
		this.error = error;
		this.json = json;
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

	/**
	 * Return the raw JSON response
	 *
	 * @return raw JSON server response
	 */
	public String getJson() {
		return this.json;
	}

}
