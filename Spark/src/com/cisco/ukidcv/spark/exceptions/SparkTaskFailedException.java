/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.exceptions;

/**
 * Thrown if there are any workflow/task related problems.
 * <p>
 * Designed to be catchable if an account name cannot be found
 *
 * @author Matt Day
 *
 */
public class SparkTaskFailedException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Throw new account exception
	 *
	 * @param args
	 */
	public SparkTaskFailedException(String args) {
		super(args);
	}
}
