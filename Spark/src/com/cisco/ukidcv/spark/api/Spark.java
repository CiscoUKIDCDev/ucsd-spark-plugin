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
import com.cisco.ukidcv.spark.api.json.SparkReport;
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
public class Spark {

	/**
	 * Gets a spark report for a given city
	 *
	 * @param account
	 *            Account to check from
	 * @param city
	 *            City name (e.g. London)
	 * @return Spark report
	 * @throws SparkReportException
	 *             if the city is not found
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkReport getSpark(SparkAccount account, String city)
			throws SparkReportException, HttpException, IOException {
		// Set up the request to the server via http on port 80:
		UCSDHttpRequest req = new UCSDHttpRequest(SparkConstants.SPARK_SERVER, "http", 80);

		// Set the request type to get:
		req.setMethodType(HttpRequestConstants.METHOD_TYPE_GET);

		// Set up proxy if it's needed:
		if (account.getProxy().isProxy()) {
			req.setProxyServer(account.getProxy().getProxyServer());
			req.setProxyPort(account.getProxy().getProxyPort());
			if (account.getProxy().isProxyAuth()) {
				req.setProxyUser(account.getProxy().getProxyUser());
				req.setProxyPass(account.getProxy().getProxyPass());
			}
		}

		// Construct query string so it includes the city and API key
		String queryString = SparkConstants.SPARK_REPORT_URI + "?q=" + city + "&APPID=" + account.getApiKey()
				+ "&units=metric";
		req.setUri(queryString);

		// Execute the request:
		req.execute();

		String response = req.getHttpResponse();

		// Check if the response is not empty:
		if (!"".equals(response)) {
			Gson gson = new Gson();
			SparkReport report = gson.fromJson(response, SparkReport.class);
			return report;
		}
		throw new SparkReportException("Could not get spark from server");
	}
}
