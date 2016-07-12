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
 * This class file represents a spark forecast from the opensparkmap.org
 * <p>
 * Each element maps 1:1 with the JSON API. See
 * <a href="http://opensparkmap.org/current">http://opensparkmap.org/current
 * </a>
 * </p>
 * <p>
 * This is not javadoc'd, please see the API documentation for detail.
 *
 * @author Matt Day
 *
 */
@SuppressWarnings("javadoc")
public class SparkReportSpark {

	private Integer id;
	private String main;
	private String description;
	private String icon;
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 *
	 * @return The id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 *
	 * @param id
	 *            The id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 *
	 * @return The main
	 */
	public String getMain() {
		return this.main;
	}

	/**
	 *
	 * @param main
	 *            The main
	 */
	public void setMain(String main) {
		this.main = main;
	}

	/**
	 *
	 * @return The description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 *
	 * @param description
	 *            The description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 *
	 * @return The icon
	 */
	public String getIcon() {
		return this.icon;
	}

	/**
	 *
	 * @param icon
	 *            The icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
