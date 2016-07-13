/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.api.json.SparkPersonDetails;
import com.cisco.ukidcv.spark.api.json.SparkRooms;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.constants.SparkConstants.httpMethod;
import com.cisco.ukidcv.spark.exceptions.SparkReportException;
import com.google.gson.Gson;

/**
 * Spark API requests - obtains raw JSON
 *
 * @author Matt Day
 *
 */
public class SparkApi {
	private static Logger logger = Logger.getLogger(SparkApi.class);

	/**
	 * Requests a list of rooms from the Spark servers and returns it in JSON
	 * format
	 *
	 * @param account
	 *            Account to request from
	 * @return List of rooms in JSON format
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
	 */
	public static String getSparkRooms(SparkAccount account) throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ROOM_URI, httpMethod.GET);
		req.execute();
		return req.getResponse();
	}

	/**
	 * Returns JSON information on the signed-in user
	 *
	 * @param account
	 *            Account to check from
	 * @return JSON formatted response from the server
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static String getSparkPerson(SparkAccount account) throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ME_URI, httpMethod.GET);
		req.execute();
		return req.getResponse();
	}

	/**
	 * Returns information on a specific user ID
	 *
	 * @param account
	 *            Account to check from
	 * @param userId
	 *            User ID to check
	 * @return JSON formatted response from the server
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static String getSparkPerson(SparkAccount account, String userId)
			throws SparkReportException, HttpException, IOException {

		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_PEOPLE_URI + userId,
				httpMethod.GET);
		req.execute();
		return req.getResponse();

	}

	/**
	 * Takes a JSON response for Spark a spark user and turns it in to a
	 * SparkPersonDetails class
	 *
	 * @param json
	 *            JSON to parse
	 * @return Spark report
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkPersonDetails
	 */
	@Deprecated
	public static SparkPersonDetails getSparkDetails(String json)
			throws SparkReportException, HttpException, IOException {

		// Check if the response is not empty:
		if (!"".equals(json)) {
			Gson gson = new Gson();
			SparkPersonDetails person = gson.fromJson(json, SparkPersonDetails.class);
			return person;
		}
		throw new SparkReportException("Could not parse JSON");
	}

	/**
	 * Tests if an account can correctly connect to Spark
	 *
	 * @param account
	 *            Account to test
	 * @return true if the connection is successful
	 */
	public static boolean testConnection(SparkAccount account) {
		try {
			String json = getSparkPerson(account);
			// Check if the response is not empty:
			if (!"".equals(json)) {
				Gson gson = new Gson();
				SparkPersonDetails person = gson.fromJson(json, SparkPersonDetails.class);
				if ((person.getId() != null) && (!"".equals(person.getId()))) {
					return true;
				}
			}
		}
		catch (SparkReportException | IOException e) {
			logger.warn("Connection test failed: " + e.getMessage());
		}
		return false;
	}

	// Set up a basic http request (no method type)
	/*
	 * private static SparkHttpConnection basicRequest(SparkAccount account) {
	 * SparkHttpConnection req = new SparkHttpConnection(account, ); // Set the
	 * request type to get:
	 *
	 * req.setContentTypeHeaders("application/json; charset=utf-8");
	 *
	 * // Set up proxy if it's needed: if (account.getProxy().isProxy()) {
	 * req.setProxyServer(account.getProxy().getProxyServer());
	 * req.setProxyPort(account.getProxy().getProxyPort()); }
	 * req.addRequestHeaders("Authorization", account.getApiKey()); return req;
	 * }
	 */

}
