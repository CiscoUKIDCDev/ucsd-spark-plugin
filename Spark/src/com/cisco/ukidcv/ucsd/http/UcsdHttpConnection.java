/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 * @author Matt Day
 *******************************************************************************/
package com.cisco.ukidcv.ucsd.http;

import java.io.IOException;

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

/**
 * Provides a generic means to connect to external http and https servers with
 * options to ignore certificate validation and to provide a proxy.
 * <p>
 * It's been designed to be easy to use and cover most use-cases with pre-built
 * methods.
 * <p>
 * This is being extended by the Spark plugin to provide easier API access, but
 * it uses this API at its core
 *
 * @author Matt Day
 *
 */

public class UcsdHttpConnection {

	private static Logger logger = Logger.getLogger(UcsdHttpConnection.class);

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
	 * HTTP method type to use (POST, GET, DELETE, PUT)
	 */
	public enum httpMethod {
		/**
		 * http POST method
		 */
		POST,
		/**
		 * http GET method
		 */
		GET,
		/**
		 * http DELETE method
		 */
		DELETE,
		/**
		 * http PUT method
		 */
		PUT
	}

	/**
	 * Protocol to use (http or https)
	 */
	public enum httpProtocol {
		/**
		 * http
		 */
		HTTP,
		/**
		 * https
		 */
		HTTPS,
	}

	/**
	 * Create a connection to the Spark API using the specified account.
	 * <p>
	 * Automatically configures the token and proxy settings.
	 *
	 * @param server
	 *            Server to connect to (e.g. cisco.com)
	 *
	 * @param path
	 *            path to request to (e.g. /v1/rooms)
	 * @param protocol
	 *            Protocol to use (http or https)
	 * @param port
	 *            Port to connect on (e.g. 443)
	 * @param method
	 *            Method to use (i.e. GET, POST, DELETE, PUT)
	 */
	public UcsdHttpConnection(String server, String path, httpProtocol protocol, int port, httpMethod method) {
		// Store the method type
		this.method = method;

		this.setServer(server);

		this.setProtocol(protocol, port);

		// Set the URI and method to the Spark Server
		this.setUri(path, this.method);
	}

	/**
	 * Create a connection to the Spark API using the specified account via a
	 * Proxy
	 * <p>
	 * Automatically configures the token and proxy settings.
	 *
	 * @param server
	 *            Server to connect to (e.g. cisco.com)
	 *
	 * @param path
	 *            path to request to (e.g. /v1/rooms)
	 * @param protocol
	 *            Protocol to use (http or https)
	 * @param port
	 *            Port to connect on (e.g. 443)
	 * @param method
	 *            Method to use (i.e. GET, POST, DELETE, PUT)
	 * @param proxy
	 *            Proxy configuration to use
	 */
	public UcsdHttpConnection(String server, String path, httpProtocol protocol, int port, httpMethod method,
			ProxySettings proxy) {
		// Store the method type
		this.method = method;

		this.setServer(server);

		this.setProtocol(protocol, port);

		// Set the URI and method to the Spark Server
		this.setUri(path, this.method);

		// Set the proxy
		this.setProxy(proxy);
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
	public UcsdHttpConnection() {
		super();
	}

	/**
	 * Set basic http authentication for this connection
	 *
	 * @param username
	 *            Username
	 * @param password
	 *            Password
	 */
	public void setAuth(String username, String password) {
		AuthScope authScope = new AuthScope(this.server, this.port);
		Credentials authCreds = new UsernamePasswordCredentials(username, password);
		this.httpclient.getCredentialsProvider().setCredentials(authScope, authCreds);
	}

	/**
	 * Configure proxy for this connection with a ProxySettings class
	 *
	 * @param proxy
	 *            Proxy configuration file to use
	 */
	public void setProxy(ProxySettings proxy) {
		// If the proxy is set:
		if (proxy.isEnabled()) {
			HttpHost proxyConnection = new HttpHost(proxy.getServer(), proxy.getPort(), "http");

			// Proxy auth is handled like http authentication
			if (proxy.isAuth()) {
				AuthScope proxyScope = new AuthScope(proxy.getServer(), proxy.getPort());
				Credentials proxyCreds = new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword());
				this.httpclient.getCredentialsProvider().setCredentials(proxyScope, proxyCreds);
			}

			// Register the proxy server with the http client handler
			this.httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyConnection);
		}
	}

	/**
	 * Configure proxy for this connection with manual inputs
	 *
	 * @param proxyServer
	 *            Proxy server to connect to
	 * @param proxyPort
	 *            Proxy server port
	 */
	public void setProxy(String proxyServer, int proxyPort) {
		this.setProxy(proxyServer, proxyPort, null, null);
	}

	/**
	 * Configure authenticated proxy for this connection with manual inputs
	 *
	 * @param proxyServer
	 *            Proxy server to connect to
	 * @param proxyPort
	 *            Proxy server port
	 * @param proxyUser
	 *            Proxy user (null for none)
	 * @param proxyPass
	 *            Proxy password (null for none)
	 */
	public void setProxy(String proxyServer, int proxyPort, String proxyUser, String proxyPass) {

		HttpHost proxyConnection = new HttpHost(proxyServer, proxyPort, "http");
		// Proxy auth is handled like http authentication
		if ((proxyUser != null) && (proxyPass != null)) {
			AuthScope proxyScope = new AuthScope(proxyServer, proxyPort);
			Credentials proxyCreds = new UsernamePasswordCredentials(proxyUser, proxyPass);
			this.httpclient.getCredentialsProvider().setCredentials(proxyScope, proxyCreds);
		}

		// Register the proxy server with the http client handler
		this.httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyConnection);
	}

	/**
	 * Allow untrusted certificates for this session? Useful if connecting to
	 * lab or internal equipment without a trusted certificate
	 *
	 * @param allow
	 *            true to allow untrusted certificates (dangerous!)
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
	 * Set the header to application/json
	 */
	public void setJsonContentHeader() {
		this.setHeader("Content-type", "application/json; charset=utf-8");
	}

	/**
	 * Sets a JSON body parameter
	 *
	 * @param body
	 */
	public void setBodyJson(String body) {
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

				// This method is deprecated, but the workaround is to
				// upgrade to 4.3 which isn't included in UCSD as of 5.5
				socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

				this.httpclient.getConnectionManager().getSchemeRegistry()
						.register(new Scheme("https", 443, socketFactory));
			}

			// Execute http request
			try {
				HttpHost target = new HttpHost(this.server, this.port, this.protocol);
				HttpResponse rsp = this.httpclient.execute(target, this.request);

				// Store response string:
				if (rsp.getEntity() != null) {
					this.response = EntityUtils.toString(rsp.getEntity());
				}
				this.httpCode = rsp.getStatusLine().getStatusCode();
			}
			finally {
				// Always release the connection
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
