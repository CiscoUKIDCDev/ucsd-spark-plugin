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
	 * Gets a list of all Spark rooms
	 *
	 * @param account
	 *            Account to check from
	 * @return Spark report
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkRooms getSparkRooms(SparkAccount account)
			throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		UCSDHttpRequest req = basicRequest(account);

		// Set URI to the current Spark API version:
		req.setUri(SparkConstants.SPARK_ROOM_URI);

		// Set method type to GET
		req.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);
		// Execute the request:
		req.execute();

		String response = req.getHttpResponse();

		// Check if the response is not empty:
		if (!"".equals(response)) {
			Gson gson = new Gson();
			SparkRooms rooms = gson.fromJson(response, SparkRooms.class);
			return rooms;
		}
		throw new SparkReportException("Could not get spark from server");
	}

	/**
	 * Returns information on the signed-in user
	 *
	 * @param account
	 *            Account to check from
	 * @return Spark report
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkPersonDetails getSparkDetails(SparkAccount account)
			throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		UCSDHttpRequest req = basicRequest(account);

		// Set URI to the current Spark API version:
		req.setUri(SparkConstants.SPARK_ME_URI);

		// Set method type to GET
		req.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);
		// Execute the request:
		req.execute();

		String response = req.getHttpResponse();

		// Check if the response is not empty:
		if (!"".equals(response)) {
			Gson gson = new Gson();
			SparkPersonDetails rooms = gson.fromJson(response, SparkPersonDetails.class);
			return rooms;
		}
		throw new SparkReportException("Could not get spark from server");
	}

	/**
	 * Returns information on a specific user ID
	 *
	 * @param account
	 *            Account to check from
	 * @param userId
	 *            User ID to check
	 * @return Spark report
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkPersonDetails getSparkDetails(SparkAccount account, String userId)
			throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		UCSDHttpRequest req = basicRequest(account);

		// Set URI to the current Spark API version:
		req.setUri(SparkConstants.SPARK_PEOPLE_URI + userId);

		// Set method type to GET
		req.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);
		// Execute the request:
		req.execute();

		String response = req.getHttpResponse();

		// Check if the response is not empty:
		if (!"".equals(response)) {
			Gson gson = new Gson();
			SparkPersonDetails rooms = gson.fromJson(response, SparkPersonDetails.class);
			return rooms;
		}
		throw new SparkReportException("Could not get spark from server");
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
}
