/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
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
	public boolean isProxy() {
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
}
