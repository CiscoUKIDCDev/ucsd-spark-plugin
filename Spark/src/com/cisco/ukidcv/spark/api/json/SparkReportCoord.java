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
public class SparkReportCoord {

	private Double lon;
	private Double lat;
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 *
	 * @return The lon
	 */
	public Double getLon() {
		return this.lon;
	}

	/**
	 *
	 * @param lon
	 *            The lon
	 */
	public void setLon(Double lon) {
		this.lon = lon;
	}

	/**
	 *
	 * @return The lat
	 */
	public Double getLat() {
		return this.lat;
	}

	/**
	 *
	 * @param lat
	 *            The lat
	 */
	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
