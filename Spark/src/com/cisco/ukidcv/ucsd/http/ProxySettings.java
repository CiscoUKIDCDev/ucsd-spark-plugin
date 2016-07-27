/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.ucsd.http;

/**
 * Class to store and manage proxy configuration - you could put this in your
 * account settings to keep the configuration simple
 *
 * @author Matt Day
 *
 */
public class ProxySettings {

	// Is the proxy enabled? By default, it's not
	private boolean enabled = false;
	// Does it need auth?
	private boolean auth = false;

	// Server properties
	private String server = null;
	private int port = 80;

	// Auth properties
	private String username = null;
	private String password = null;

	/**
	 * Create an empty proxy settings class - it will default to not enabling
	 * the proxy
	 */
	public ProxySettings() {

	}

	/**
	 * Create new proxy settings with a server and port to connect to. No
	 * authentication will be enabled
	 *
	 * @param server
	 * @param port
	 */
	public ProxySettings(String server, int port) {
		this.enabled = true;
		this.auth = false;
		this.server = server;
		this.port = port;
	}

	/**
	 * Create new proxy settings with a server, port, username and password.
	 * Authentication will be enabled
	 *
	 * @param server
	 * @param port
	 * @param username
	 * @param password
	 */
	public ProxySettings(String server, int port, String username, String password) {
		this.enabled = true;
		this.auth = true;
		this.server = server;
		this.port = port;
		this.username = username;
		this.password = password;
	}

	/**
	 * @return true if the proxy should be used
	 */
	public boolean isEnabled() {
		return this.enabled;
	}

	/**
	 * Set if the proxy should be enabled or not
	 *
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return true if the authentication should be used
	 */
	public boolean isAuth() {
		return this.auth;
	}

	/**
	 * Set if proxy authentication should be enabled or not
	 *
	 * @param auth
	 */
	public void setAuth(boolean auth) {
		this.auth = auth;
	}

	/**
	 * @return Proxy server address
	 */
	public String getServer() {
		return this.server;
	}

	/**
	 * Sets the proxy server
	 *
	 * @param server
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return Proxy tcp port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * Sets the proxy server tcp port
	 *
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return Proxy authentication username
	 */
	public String getUsername() {
		return this.username;
	}

	/**
	 * Sets the proxy username
	 *
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Proxy authentication password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * Sets the proxy password
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
