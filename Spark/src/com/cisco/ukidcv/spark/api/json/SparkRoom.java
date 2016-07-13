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
public class SparkRoom {

	private String id;
	private String title;
	private String type;
	private Boolean isLocked;
	private String lastActivity;
	private String created;
	private String teamId;
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 *
	 * @return The id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 *
	 * @param id
	 *            The id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 *
	 * @return The title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 *
	 * @param title
	 *            The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 *
	 * @return The type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 *
	 * @param type
	 *            The type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 *
	 * @return The isLocked
	 */
	public Boolean getIsLocked() {
		return this.isLocked;
	}

	/**
	 *
	 * @param isLocked
	 *            The isLocked
	 */
	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 *
	 * @return The lastActivity
	 */
	public String getLastActivity() {
		return this.lastActivity;
	}

	/**
	 *
	 * @param lastActivity
	 *            The lastActivity
	 */
	public void setLastActivity(String lastActivity) {
		this.lastActivity = lastActivity;
	}

	/**
	 *
	 * @return The created
	 */
	public String getCreated() {
		return this.created;
	}

	/**
	 *
	 * @param created
	 *            The created
	 */
	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 *
	 * @return The teamId
	 */
	public String getTeamId() {
		return this.teamId;
	}

	/**
	 *
	 * @param teamId
	 *            The teamId
	 */
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
