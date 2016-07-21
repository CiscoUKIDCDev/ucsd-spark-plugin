/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account;

/**
 * UCS Director stores account information in JSON format.
 * <p>
 * This class is used to represent that back as Java. Each item you store in
 * your account needs to be represented here.
 * <p>
 * This occurs in SparkAccount but can also be extracted by using
 * acc.getCredential();
 * <p>
 * Not javadoc'd as the values are documented in the Database
 *
 * @author Matt Day
 * @see com.cisco.ukidcv.spark.account.SparkReportDB
 *
 */
@SuppressWarnings("javadoc")
public class SparkAccountJsonObject {
	private String account;
	private String apiKey;
	private boolean proxy;
	private String proxyServer;
	private int proxyPort;
	private boolean proxyAuth;
	private String proxyUser;
	private String proxyPass;

	public String getAccount() {
		return this.account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getApiKey() {
		return this.apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public boolean isProxy() {
		return this.proxy;
	}

	public void setProxy(boolean proxy) {
		this.proxy = proxy;
	}

	public String getProxyServer() {
		return this.proxyServer;
	}

	public void setProxyServer(String proxyServer) {
		this.proxyServer = proxyServer;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public boolean isProxyAuth() {
		return this.proxyAuth;
	}

	public void setProxyAuth(boolean proxyAuth) {
		this.proxyAuth = proxyAuth;
	}

	public String getProxyUser() {
		return this.proxyUser;
	}

	public void setProxyUser(String proxyUser) {
		this.proxyUser = proxyUser;
	}

	public String getProxyPass() {
		return this.proxyPass;
	}

	public void setProxyPass(String proxyPass) {
		this.proxyPass = proxyPass;
	}

}
