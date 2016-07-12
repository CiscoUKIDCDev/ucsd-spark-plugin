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
public class SparkReportWind {

	private Double speed;
	private Integer deg;
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 *
	 * @return The speed
	 */
	public Double getSpeed() {
		return this.speed;
	}

	/**
	 *
	 * @param speed
	 *            The speed
	 */
	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	/**
	 *
	 * @return The deg
	 */
	public Integer getDeg() {
		return this.deg;
	}

	/**
	 *
	 * @param deg
	 *            The deg
	 */
	public void setDeg(Integer deg) {
		this.deg = deg;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
