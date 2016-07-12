/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.json.SparkPersonDetails;
import com.cisco.ukidcv.spark.api.json.SparkRooms;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkReportException;
import com.google.gson.Gson;
import com.rwhitear.ucsdHttpRequest.UCSDHttpRequest;
import com.rwhitear.ucsdHttpRequest.constants.HttpRequestConstants;

/**
 * Gets the spark forecast for a particular city
 *
 * @author Matt Day
 *
 */
public class SparkApi {

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
	public static String getSparkRoomsJson(SparkAccount account)
			throws SparkReportException, HttpException, IOException {

		// Check inventory:
		SparkInventory.getRooms(account);

		// Set up a request to the spark server
		UCSDHttpRequest req = basicRequest(account);

		// Set URI to the current Spark API version:
		req.setUri(SparkConstants.SPARK_ROOM_URI);

		// Set method type to GET
		req.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);
		// Execute the request:
		req.execute();

		return req.getHttpResponse();
	}

	/**
	 * Takes a JSON response for Spark rooms and turns it in to a SparkRooms
	 * class
	 *
	 * @param json
	 *            JSON response from server
	 * @return Java formatted list of rooms
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
	 */
	public static SparkRooms getSparkRooms(String json) throws SparkReportException, HttpException, IOException {

		// Check if the response is not empty:
		if (!"".equals(json)) {
			Gson gson = new Gson();
			SparkRooms rooms = gson.fromJson(json, SparkRooms.class);
			return rooms;
		}
		throw new SparkReportException("Could not parse JSON");
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
	public static String getSparkDetailsJson(SparkAccount account)
			throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		UCSDHttpRequest req = basicRequest(account);

		// Set URI to the current Spark API version:
		req.setUri(SparkConstants.SPARK_ME_URI);

		// Set method type to GET
		req.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);
		// Execute the request:
		req.execute();

		return req.getHttpResponse();
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
	public static String getSparkDetailsJson(SparkAccount account, String userId)
			throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		UCSDHttpRequest req = basicRequest(account);

		// Set URI to the current Spark API version:
		req.setUri(SparkConstants.SPARK_PEOPLE_URI + userId);

		// Set method type to GET
		req.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);
		// Execute the request:
		req.execute();

		return req.getHttpResponse();

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
	public static SparkPersonDetails getSparkDetails(String json)
			throws SparkReportException, HttpException, IOException {

		// Check if the response is not empty:
		if (!"".equals(json)) {
			Gson gson = new Gson();
			SparkPersonDetails rooms = gson.fromJson(json, SparkPersonDetails.class);
			return rooms;
		}
		throw new SparkReportException("Could not parse JSON");
	}

	// Set up a basic http request (no method type)
	private static UCSDHttpRequest basicRequest(SparkAccount account) {
		UCSDHttpRequest req = new UCSDHttpRequest(SparkConstants.SPARK_SERVER, "https", 443);
		// Set the request type to get:

		req.setContentTypeHeaders("application/json; charset=utf-8");

		// Set up proxy if it's needed:
		if (account.getProxy().isProxy()) {
			req.setProxyServer(account.getProxy().getProxyServer());
			req.setProxyPort(account.getProxy().getProxyPort());
			if (account.getProxy().isProxyAuth()) {
				req.setProxyUser(account.getProxy().getProxyUser());
				req.setProxyPass(account.getProxy().getProxyPass());
			}
		}
		req.addRequestHeaders("Authorization", account.getApiKey());
		return req;
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
			SparkPersonDetails person = getSparkDetails(getSparkDetailsJson(account));
			if ((person.getId() != null) && (!"".equals(person.getId()))) {
				return true;
			}
		}
		catch (SparkReportException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
