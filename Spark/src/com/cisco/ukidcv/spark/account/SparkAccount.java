/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account;

import com.cisco.cuic.api.client.JSON;
import com.cisco.ukidcv.spark.exceptions.SparkAccountException;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.ReportContext;

/**
 * Acts as the interface between the SparkAccountDB class and the rest of the
 * system. It stores the account name and API key for that account.
 * <p>
 * Has multiple constructors allowing it to be used from almost anywhere.
 *
 * @author Matt Day
 *
 */
public class SparkAccount {

	// API key
	private String apiKey;

	private SparkProxySettings proxy;

	// Account name (must be set by constructor)
	private final String accountName;

	/**
	 * Used for testing classes only - not for use in regular code
	 *
	 * @param apiKey
	 *            API key for testing
	 * @param test
	 *            bogus boolean confirming this is a test case (so it doesn't
	 *            conflict with the below accountName constructor)
	 */
	@Deprecated
	public SparkAccount(String apiKey, boolean test) {
		this.apiKey = apiKey;
		// Set the account
		this.accountName = "Testing";
		// Set up proxy blank
		this.proxy = new SparkProxySettings(null);
	}

	/**
	 * Obtain credentials from the account name
	 * <p>
	 * For example, if a user creates an account 'Spark-1' then this will obtain
	 * the credentials for that account
	 *
	 * @param accountName
	 *            Name of the Spark account
	 * @throws SparkAccountException
	 *             If the account cannot be found
	 */
	public SparkAccount(String accountName) throws SparkAccountException {
		this.accountName = accountName;
		try {
			// Get the account details
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(this.accountName);

			if (acc == null) {
				// Account does not exist:
				throw new SparkAccountException("Unable to find the account:" + this.accountName);
			}
			// Initialise from PhysicalInfraAccount
			this.initFromAccount(acc);
		}
		catch (Exception e) {
			throw new SparkAccountException("Unable to find the account:" + this.accountName + " " + e.getMessage());
		}
	}

	/**
	 * Each report (drop down, list of values etc) has a ReportContext. This can
	 * be used to obtain the account credentials
	 * <p>
	 * For example, if a user has two accounts (Spark-1 and Spark-2) and is
	 * currently looking at a table for Spark-1 this will return those
	 * credentials
	 *
	 * @param context
	 *            Current context
	 * @throws SparkAccountException
	 *             If the account isn't found
	 */
	public SparkAccount(ReportContext context) throws SparkAccountException {
		final String contextId = context.getId();
		// If the context ID isn't null, use it to get the account name:
		this.accountName = (contextId == null) ? null : contextId.split(";")[0];

		if (this.accountName == null) {
			throw new SparkAccountException("Account not found");
		}
		try {
			PhysicalInfraAccount acc = AccountUtil.getAccountByName(this.accountName);
			if (acc == null) {
				throw new SparkAccountException("Unable to find the account:" + this.accountName);
			}
			this.initFromAccount(acc);
		}
		catch (Exception e) {
			throw new SparkAccountException("Unable to find the account:" + this.accountName + " " + e.getMessage());
		}

	}

	// Use the internal account JSON to obtain them as native Java objects:

	private void initFromAccount(PhysicalInfraAccount acc) throws Exception {
		// Get account information as a JSON query:
		String json = acc.getCredential();
		// Parse using SparkAccountJsonObject as a template:
		SparkAccountJsonObject account = (SparkAccountJsonObject) JSON.jsonToJavaObject(json,
				SparkAccountJsonObject.class);

		// Set API key:
		this.apiKey = account.getApiKey();
		// Set proxy
		this.proxy = new SparkProxySettings(account);
	}

	/**
	 * Get the API key for an account
	 *
	 * @return API key
	 */
	public String getApiKey() {
		return this.apiKey;
	}

	/**
	 * Get the account name for an account
	 *
	 * @return Account Name
	 */
	public String getAccountName() {
		return this.accountName;
	}

	/**
	 * Get Proxy information for this account
	 *
	 * @return Proxy info
	 */
	public SparkProxySettings getProxy() {
		return this.proxy;
	}

	/**
	 * Set Proxy information for this account
	 *
	 * @param proxySettings
	 *            proxy settings to use
	 */
	public void setProxy(SparkProxySettings proxySettings) {
		this.proxy = proxySettings;
	}

}
