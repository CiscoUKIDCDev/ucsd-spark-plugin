/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.exceptions;

/**
 * Thrown if there are any spark report related problems.
 * <p>
 * Designed to be catchable if a spark report fails to be returned from the
 * server
 *
 * @author Matt Day
 *
 */
public class SparkReportException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Throw new account exception
	 *
	 * @param args
	 */
	public SparkReportException(String args) {
		super(args);
	}
}
