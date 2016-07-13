/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.SparkProxySettings;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.constants.SparkConstants.httpMethod;

/**
 * Handles communication to the Spark servers.
 * <p>
 * Can be used in two ways, the first where the constructor takes the account,
 * uri and method is the easiest but is restricted in flexibility. It will add
 * proxy settings and the token headers automatically.
 * <p>
 * The default constructor does no setup (not even proxy) and can be used for
 * more advanced workloads.
 *
 * @author Matt Day
 *
 */
public class SparkHttpConnection {
	private static Logger logger = Logger.getLogger(SparkHttpConnection.class);

	private HttpMethod request;
	private String response = null;
	private HttpClient httpclient = new HttpClient();

	/**
	 * Create a connection to the Spark API using the specified account.
	 * <p>
	 * Automatically configures the token and proxy settings.
	 *
	 * @param account
	 *            Account from which to connect
	 * @param uri
	 *            URI to connect to (e.g. /v1/rooms)
	 * @param method
	 *            Method to use (i.e. GET, POST, DELETE, PUT)
	 */
	public SparkHttpConnection(SparkAccount account, String uri, httpMethod method) {
		// Set the http target to the Spark server:
		String fullUri = SparkConstants.SPARK_SERVER_PROTOCOL + "://" + SparkConstants.SPARK_SERVER_HOSTNAME + uri;
		this.setUri(fullUri, method);

		// Add Spark token
		this.setHeader("Authorization", account.getApiKey());

		// Do we need a proxy?
		this.setProxy(account.getProxy());
	}

	/**
	 * Create a new http entry with no configuration. You will need to call
	 * setServer() etc yourself
	 */
	public SparkHttpConnection() {
		super();
	}

	/**
	 * Add proxy settings
	 *
	 * @param proxy
	 *            Proxy settings
	 */
	public void setProxy(SparkProxySettings proxy) {
		if (proxy.isProxy()) {
			this.httpclient.getHostConfiguration().setProxy(proxy.getProxyServer(), proxy.getProxyPort());
			if (proxy.isProxyAuth()) {
				this.httpclient.getState().setProxyCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(proxy.getProxyUser(), proxy.getProxyPass()));
			}
		}
	}

	/**
	 * Set an http header
	 *
	 * @param key
	 *            Key (e.g. "Content-type")
	 * @param value
	 *            Value (e.g. "text/plain")
	 */
	public void setHeader(String key, String value) {
		this.request.addRequestHeader(key, value);
	}

	/**
	 * Set the URI and method to use
	 *
	 * @param uri
	 *            URI (e.g. https://api.ciscospark.com/v1/rooms)
	 * @param method
	 *            http method Method to use (i.e. GET, POST, DELETE, PUT)
	 */
	public void setUri(String uri, httpMethod method) {
		switch (method) {
		case GET:
			this.request = new GetMethod(uri);
			return;
		case PUT:
			this.request = new PutMethod(uri);
			return;
		case POST:
			this.request = new PostMethod(uri);
			return;
		case DELETE:
			this.request = new DeleteMethod(uri);
			return;
		default:
			logger.error("Unknown method type " + method);
			return;
		}
	}

	/**
	 * Execute the request
	 *
	 * @throws ClientProtocolException
	 *             If there is a connectivity problem
	 * @throws IOException
	 *             If there is an IO connectivity problem
	 */
	public void execute() throws ClientProtocolException, IOException {
		try {
			this.httpclient.executeMethod(this.request);
			this.response = this.request.getResponseBodyAsString();
		}
		finally {
			this.request.releaseConnection();
		}
	}

	/**
	 * Return the http response
	 *
	 * @return http response
	 */
	public String getResponse() {
		return this.response;
	}

}
