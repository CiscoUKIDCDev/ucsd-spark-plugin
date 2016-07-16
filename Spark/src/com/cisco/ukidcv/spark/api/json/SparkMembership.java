/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api.json;

import java.util.HashMap;
import java.util.Map;

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
public class SparkMembership {

	private String id;
	private String roomId;
	private String personId;
	private String personEmail;
	private String personDisplayName;
	private Boolean isModerator;
	private Boolean isMonitor;
	private String created;
	private Map<String, Object> additionalProperties = new HashMap<>();

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoomId() {
		return this.roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
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

	public String getPersonDisplayName() {
		return this.personDisplayName;
	}

	public void setPersonDisplayName(String personDisplayName) {
		this.personDisplayName = personDisplayName;
	}

	public Boolean isModerator() {
		return this.isModerator;
	}

	public void setIsModerator(Boolean isModerator) {
		this.isModerator = isModerator;
	}

	public Boolean getIsMonitor() {
		return this.isMonitor;
	}

	public void setIsMonitor(Boolean isMonitor) {
		this.isMonitor = isMonitor;
	}

	public String getCreated() {
		return this.created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

}
