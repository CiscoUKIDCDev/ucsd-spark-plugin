/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account;

/**
 * This class stores proxy settings for communicating with the outside world.
 *
 * @author Matt Day
 *
 */
public class SparkProxySettings {
	private boolean proxy;
	private String proxyServer = null;
	private int proxyPort;
	private boolean proxyAuth;
	private String proxyUser = null;
	private String proxyPass = null;

	/**
	 * @return If the proxy is set
	 */
	public boolean isEnabled() {
		return this.proxy;
	}

	/**
	 * @return the proxy server name
	 */
	public String getProxyServer() {
		return this.proxyServer;
	}

	/**
	 * @return the proxy port number
	 */
	public int getProxyPort() {
		return this.proxyPort;
	}

	/**
	 * @return if proxy authentication is set
	 */
	public boolean isProxyAuth() {
		return this.proxyAuth;
	}

	/**
	 * @return the proxy username
	 */
	public String getProxyUser() {
		return this.proxyUser;
	}

	/**
	 * @return the proxy password
	 */
	public String getProxyPass() {
		return this.proxyPass;
	}

	/**
	 * Set if a proxy should be used
	 *
	 * @param proxy
	 *            true to use a proxy
	 */
	public void setProxy(boolean proxy) {
		this.proxy = proxy;
	}

	/**
	 * Set the proxy server to be used
	 *
	 * @param proxyServer
	 *            proxy server
	 */
	public void setProxyServer(String proxyServer) {
		this.proxyServer = proxyServer;
	}

	/**
	 * Set the proxy port to be used
	 *
	 * @param proxyPort
	 *            proxy tcp port
	 */
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	/**
	 * Set if proxy authentication should be used
	 *
	 * @param proxyAuth
	 *            true to use authentication
	 */
	public void setProxyAuth(boolean proxyAuth) {
		this.proxyAuth = proxyAuth;
	}

	/**
	 * Set the proxy user to be used
	 *
	 * @param proxyUser
	 *            proxy username to use
	 */
	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	/**
	 * Set the proxy password to be used
	 *
	 * @param proxyPass
	 *            proxy password
	 */
	public void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}

	/**
	 * Stores proxy information - should not normally be called directly,
	 * instead obtain it from the SparkAccount
	 *
	 * @param account
	 *            Account to get it from
	 * @see com.cisco.ukidcv.spark.account.SparkAccount
	 */
	public SparkProxySettings(SparkAccountJsonObject account) {
		if (account == null) {
			this.proxy = false;
			this.proxyAuth = false;
			return;
		}
		this.proxy = account.isProxy();
		this.proxyAuth = account.isProxyAuth();
		this.proxyServer = account.getProxyServer();
		this.proxyPass = account.getProxyPass();
		this.proxyUser = account.getProxyUser();
		this.proxyPort = account.getProxyPort();
	}

	/**
	 * Stores proxy information - can be used for test purposes
	 *
	 * @see com.cisco.ukidcv.spark.account.SparkAccount#setProxy
	 *
	 */
	public SparkProxySettings() {
		this.proxy = false;
		this.proxyAuth = false;
	}
}
