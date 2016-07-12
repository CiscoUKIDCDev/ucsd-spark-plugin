/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
/**
 *
 */
package com.cisco.ukidcv.spark.account;

import java.util.List;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.apache.log4j.Logger;

import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.account.AbstractInfraAccount;
import com.cloupia.model.cIM.FormFieldDefinition;
import com.cloupia.model.cIM.InfraAccount;
import com.cloupia.service.cIM.inframgr.collector.view2.ConnectorCredential;
import com.cloupia.service.cIM.inframgr.forms.wizard.FormField;

/**
 * This class stores account details in the UCS Director database.
 * <p>
 *
 * It also indirectly provides the form to create the account with the JDO
 * notation below. The form fields are shown when the user creates a new
 * physical account
 * <p>
 * <b>Note:</b> You should ensure there's a jdo.files entry present and that it
 * contains this class name
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "spark_account")
public class SparkAccountDB extends AbstractInfraAccount implements ConnectorCredential {

	// Log entries for debugging purposes
	static Logger logger = Logger.getLogger(SparkAccountDB.class);

	@Persistent
	private boolean isCredentialPolicy = false;

	// Input field for API key:
	@Persistent
	@FormField(label = "Spark API Key", help = "API Key from developer.ciscospark.com", mandatory = true, type = FormFieldDefinition.FIELD_TYPE_PASSWORD)
	private String apiKey;

	// Input field for API key:
	@Persistent
	@FormField(label = "Use a proxy", help = "Check if you need to use a proxy to access the internet", type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	private boolean proxy;

	// Input field for API key:
	@Persistent
	@FormField(label = "Proxy Server", help = "Proxy server to use (leave blank if none)", mandatory = false)
	private String proxyServer;

	// Input field for API key:
	@Persistent
	@FormField(label = "Proxy Port", help = "Proxy port to use (leave blank if none)", mandatory = false)
	private int proxyPort;

	// Input field for API key:
	@Persistent
	@FormField(label = "Proxy Authentication", help = "Check if your proxy server requires authentication", mandatory = false, type = FormFieldDefinition.FIELD_TYPE_BOOLEAN)
	private boolean proxyAuth;

	// Input field for API key:
	@Persistent
	@FormField(label = "Proxy Username", help = "Proxy username (leave blank if none)", mandatory = false)
	private String proxyUser;

	// Input field for API key:
	@Persistent
	@FormField(label = "Proxy Password", help = "Proxy password (leave blank if none)", type = FormFieldDefinition.FIELD_TYPE_PASSWORD, mandatory = false)
	private String proxyPass;

	/**
	 * Initialises the account DB. You should use this to set any defaults
	 * (don't set them above as it will break persistency).
	 */
	public SparkAccountDB() {
		super();
		// Set the port to 80 by default in the GUI
		this.proxyPort = 80;
	}

	/*
	 * Treat the API key as a password
	 */
	@Override
	public String getPassword() {
		return this.apiKey;
	}

	@Override
	public void setPassword(String apiKey) {
		this.apiKey = apiKey;
	}

	/*
	 * No policy for this plugin, so return null:
	 */
	@Override
	public String getPolicy() {
		return null;
	}

	@Override
	public void setPolicy(String arg0) {
		// Do nothing - not implemented

	}

	/*
	 * Credential policies are out of the scope of this plugin, so they are not
	 * implemented
	 */
	@Override
	public boolean isCredentialPolicy() {
		return false;
	}

	@Override
	public void setCredentialPolicy(boolean arg0) {
		// Do nothing - not implemented
	}

	/*
	 * Plugins let you set the TCP port you wish to connect to - not needed for
	 * this either
	 */
	@Override
	public void setPort(int arg0) {
		// Do nothing

	}

	/*
	 * We're not using usernames - just a simple API key
	 */
	@Override
	public void setUsername(String arg0) {
		// Do nothing

	}

	/*
	 * Stores the account in the database
	 */
	@Override
	public InfraAccount toInfraAccount() {
		try {
			// Create an object store
			ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
			// Generate a simple query to store:
			String cquery = "apiKey == '" + this.apiKey + "' && proxy == " + this.proxy + " && proxyPass == '"
					+ this.proxyPass + "' && proxyUser == '" + this.proxyUser + "' && proxyPort == " + this.proxyPort
					+ " && proxyServer == '" + this.proxyServer + "' && proxyAuth == " + this.proxyAuth;
			List<InfraAccount> accList = store.query(cquery);
			if ((accList != null) && (accList.size() > 0)) {
				return accList.get(0);
			}
			return null;

		}
		catch (Exception e) {
			logger.error("Exception while mapping DeviceCredential to InfraAccount for server: " + this.apiKey + ": "
					+ e.getMessage());
		}

		return null;
	}

}
