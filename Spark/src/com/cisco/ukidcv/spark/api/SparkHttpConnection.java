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
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.protocol.Protocol;
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
	private int httpCode;
	private HttpClient httpclient = new HttpClient();
	private httpMethod method;

	// By default assume it's not via a proxy
	private boolean isProxied = false;

	// By default do not allow untrusted certificates
	private boolean allowUntrustedCertificates = false;

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
		// Store the method type
		this.method = method;

		// Set the http target to the Spark server:
		String fullUri = SparkConstants.SPARK_SERVER_PROTOCOL + "://" + SparkConstants.SPARK_SERVER_HOSTNAME + path;
		this.setUri(fullUri, this.method);

		// Add Spark token
		this.setHeader("Authorization", account.getApiKey());

		this.setHeader("Content-type", "application/json; charset=utf-8");

		// Do we need a proxy?
		this.setProxy(account.getProxy());
	}

	/**
	 * Create a new http entry with no configuration. You will need to call
	 * setUri() etc yourself
	 *
	 * @see #setUri
	 * @see #setProxy
	 * @see #setHeader
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
		this.isProxied = proxy.isEnabled();
		// If the proxy is set:
		if (proxy.isEnabled()) {
			this.httpclient.getHostConfiguration().setProxy(proxy.getProxyServer(), proxy.getProxyPort());
			if (proxy.isProxyAuth()) {
				this.httpclient.getState().setProxyCredentials(AuthScope.ANY,
						new UsernamePasswordCredentials(proxy.getProxyUser(), proxy.getProxyPass()));
			}
		}
	}

	/**
	 * Allow untrusted certificates for this session? Useful if connecting to
	 * lab or internal equipment without a trusted certificate
	 *
	 * @param allow
	 *            true to allow untrusted certificates
	 */
	public void allowUntrustedCertificates(boolean allow) {
		this.allowUntrustedCertificates = allow;
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
	 * Sets a body parameter
	 *
	 * @param body
	 */
	public void setBody(String body) {
		try {
			// Attempt to cast it to an EntityEnclosingMethod which supports
			// body elements (i.e. POST, PUT methods) and set the body
			((EntityEnclosingMethod) this.request)
					.setRequestEntity(new StringRequestEntity(body, "application/json", "utf-8"));
		}
		catch (Exception e) {
			logger.error("Cannot add http body to request: " + e.getMessage());
		}
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
			// If SSL verification is disabled, use own socket factory
			if (this.allowUntrustedCertificates) {
				/*
				 * There is a bug I can't seem to workaround which means if
				 * you're using a proxy you cannot provide your own socket
				 * factory... For now I'm not trying to provide a socket
				 * factory, but it will break any self-signed pages that are
				 * requested via a proxy
				 */
				if (this.isProxied) {
					Protocol.unregisterProtocol("https");
				}
				else {
					Protocol.registerProtocol("https", new Protocol("https", new UntrustedSSLSocketFactory(), 443));
				}
			}
			else {
				// Unregister any https protocol set elsewhere - otherwise it
				// may attempt to use an SSL socket factory created by another
				// plugin
				Protocol.unregisterProtocol("https");
			}
			try {
				this.httpclient.executeMethod(this.request);
				this.response = this.request.getResponseBodyAsString();
				this.httpCode = this.request.getStatusCode();
			}
			finally {
				this.request.releaseConnection();
			}
		}
		catch (Exception e) {
			logger.error("Failed to execute http request: " + e.getMessage());
		}
	}

	/**
	 * Return the http response body
	 *
	 * @return http response
	 */
	public String getResponse() {
		return this.response;
	}

	/**
	 * Return the http response code
	 *
	 * @return http response code
	 */
	public int getResponseCode() {
		return this.httpCode;
	}

}
