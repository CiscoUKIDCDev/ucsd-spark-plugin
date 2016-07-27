/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.SparkProxySettings;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.ucsd.http.UcsdHttpConnection;

/**
 * Handles communication to the Spark servers.
 * <p>
 * Extends tue UcsdHttpConnection library to handle most tasks
 *
 * @author Matt Day
 * @see UcsdHttpConnection
 *
 */
public class SparkHttpConnection extends UcsdHttpConnection {

	/**
	 * Create a connection to the Spark API using the specified account.
	 * <p>
	 * Automatically configures the token and proxy settings.
	 *
	 * @param account
	 *            Account from which to connect
	 * @param path
	 *            path to request to (e.g. /v1/rooms)
	 * @param method
	 *            Method to use (i.e. GET, POST, DELETE, PUT)
	 */
	public SparkHttpConnection(SparkAccount account, String path, httpMethod method) {
		super();

		this.setServer(SparkConstants.SPARK_SERVER_HOSTNAME);

		// Set the URI and method to the Spark Server
		this.setUri(path, method);

		// Add Spark token
		this.setHeader("Authorization", account.getApiKey());

		this.setHeader("Content-type", "application/json; charset=utf-8");

		// Do we need a proxy?
		this.setProxy(account.getProxy());
	}

	/**
	 * Add spark account proxy settings
	 *
	 * @param proxy
	 *            Proxy settings
	 */
	public void setProxy(SparkProxySettings proxy) {
		// If the proxy is set:
		if (proxy.isEnabled()) {
			// Proxy auth is handled like http authentication
			if (proxy.isProxyAuth()) {
				this.setProxy(proxy.getProxyServer(), proxy.getProxyPort(), proxy.getProxyUser(), proxy.getProxyPass());
			}
			else {
				this.setProxy(proxy.getProxyServer(), proxy.getProxyPort());
			}
		}
	}
}
