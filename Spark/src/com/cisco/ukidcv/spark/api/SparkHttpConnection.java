/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

import java.io.IOException;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.SparkProxySettings;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.constants.SparkConstants.httpMethod;
import com.cisco.ukidcv.spark.constants.SparkConstants.httpProtocol;

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

	private HttpRequestBase request;

	private String response = null;
	private int httpCode;
	private DefaultHttpClient httpclient = new DefaultHttpClient();
	private httpMethod method;

	// Default to https on 443
	private String protocol = "https";
	private int port = 443;
	private String server;

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

		this.setServer(SparkConstants.SPARK_SERVER_HOSTNAME);

		// Set the URI and method to the Spark Server
		this.setUri(path, this.method);

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
	 * @see #setServer
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
		// If the proxy is set:
		if (proxy.isEnabled()) {
			HttpHost proxyConnection = new HttpHost(proxy.getProxyServer(), proxy.getProxyPort(), "http");
			if (proxy.isProxyAuth()) {
				AuthScope proxyScope = new AuthScope(proxy.getProxyServer(), proxy.getProxyPort());
				Credentials proxyCreds = new UsernamePasswordCredentials(proxy.getProxyUser(), proxy.getProxyPass());
				this.httpclient.getCredentialsProvider().setCredentials(proxyScope, proxyCreds);
			}
			this.httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyConnection);
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
		this.request.addHeader(key, value);
	}

	/**
	 * Sets a JSON body parameter
	 *
	 * @param body
	 */
	public void setJsonBody(String body) {
		this.setBody(body, ContentType.APPLICATION_JSON);
	}

	/**
	 * Sets a body parameter
	 *
	 * @param body
	 *            message body to add
	 * @param contentType
	 *            Content type
	 */
	public void setBody(String body, ContentType contentType) {
		try {
			// Attempt to cast it to an EntityEnclosingMethod which supports
			// body elements (i.e. POST, PUT methods) and set the body
			((HttpEntityEnclosingRequestBase) this.request).setEntity(new StringEntity(body, contentType));
		}
		catch (Exception e) {
			logger.error("Cannot add http body to request: " + e.getMessage());
		}
	}

	/**
	 * Set the URI and method to use
	 *
	 * @param path
	 *            path (e.g. /api.ciscospark.com/v1/rooms)
	 * @param method
	 *            http method Method to use (i.e. GET, POST, DELETE, PUT)
	 */
	public void setUri(String path, httpMethod method) {
		switch (method) {
		case GET:
			this.request = new HttpGet(path);
			return;
		case PUT:
			this.request = new HttpPut(path);
			return;
		case POST:
			this.request = new HttpPost(path);
			return;
		case DELETE:
			this.request = new HttpDelete(path);
			return;
		default:
			logger.error("Unknown method type " + method);
			return;
		}
	}

	/**
	 * Set the server to connect to (e.g. api.ciscospark.com)
	 *
	 * @param server
	 *            server to connect to
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * Set the protocol to connect with (http or https)
	 *
	 * @param protocol
	 *            protocol to connect with
	 * @param port
	 *            TCP port to use
	 */
	public void setProtocol(httpProtocol protocol, int port) {
		this.protocol = (protocol == httpProtocol.HTTPS) ? "https" : "http";
		this.port = port;
	}

	/**
	 * Execute the request
	 *
	 * @throws ClientProtocolException
	 *             If there is a connectivity problem
	 * @throws IOException
	 *             If there is an IO connectivity problem
	 */
	@SuppressWarnings("deprecation")
	public void execute() throws ClientProtocolException, IOException {
		try {
			// If SSL verification is disabled, use own socket factory
			if (this.allowUntrustedCertificates) {
				// Create a new socket factory and set it to always say yes
				SSLSocketFactory socketFactory = new SSLSocketFactory((chain, authType) -> true);

				// This method is deprecated, but the workaround is to upgrade
				// to 4.3 which isn't included in UCSD as of 5.5
				socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				this.httpclient.getConnectionManager().getSchemeRegistry()
						.register(new Scheme("https", 443, socketFactory));
			}
			try {
				HttpHost target = new HttpHost(this.server, this.port, this.protocol);
				HttpResponse rsp = this.httpclient.execute(target, this.request);
				this.response = EntityUtils.toString(rsp.getEntity());
				this.httpCode = rsp.getStatusLine().getStatusCode();
			}
			finally {
				this.request.releaseConnection();
			}
		}
		catch (Exception e) {
			logger.error("Failed to execute http request: " + e.getMessage());
		}
		finally {
			// ALWAYS reset the https handler for other plugins
			Protocol.unregisterProtocol("https");
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
