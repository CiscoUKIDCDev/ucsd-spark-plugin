/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api.json;

/**
 * This implements the JSON from the Spark API directly in Java.
 * <p>
 * There is no Javadoc here. See the API documentation at
 * <a href="https://developer.ciscospark.com">developer.ciscospark.com</a>
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class SparkTeamMembership {
	private String id;
	private String teamId;
	private String personId;
	private String personDisplayName;
	private String personEmail;
	private Boolean isModerator;
	private String created;

	public String getId() {
		return this.id;
	}

	public String getPersonDisplayName() {
		return this.personDisplayName;
	}

	public void setPersonDisplayName(String personDisplayName) {
		this.personDisplayName = personDisplayName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTeamId() {
		return this.teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getPersonId() {
		return this.personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getPersonEmail() {
		return this.personEmail;
	}

	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}

	public Boolean isModerator() {
		return this.isModerator;
	}

	public void setModerator(Boolean isModerator) {
		this.isModerator = isModerator;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}
