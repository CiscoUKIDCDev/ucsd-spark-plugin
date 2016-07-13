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
public class SparkMembershipCreation {
	String roomId;
	String personId;
	String personEmail;
	boolean isModerator;

	public SparkMembershipCreation(boolean isModerator) {
		super();

		this.isModerator = isModerator;
	}

	public SparkMembershipCreation(String roomId, String personEmail, boolean isModerator) {
		super();
		this.roomId = roomId;
		this.personEmail = personEmail;
		this.isModerator = isModerator;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public void setPersonEmail(String personEmail) {
		this.personEmail = personEmail;
	}

	public void setModerator(boolean isModerator) {
		this.isModerator = isModerator;
	}

	public String getRoomId() {
		return this.roomId;
	}

	public String getPersonId() {
		return this.personId;
	}

	public String getPersonEmail() {
		return this.personEmail;
	}

	public boolean isModerator() {
		return this.isModerator;
	}

}
