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
public class SparkReportSys {

	private Integer type;
	private Integer id;
	private Double message;
	private String country;
	private Integer sunrise;
	private Integer sunset;
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 *
	 * @return The type
	 */
	public Integer getType() {
		return this.type;
	}

	/**
	 *
	 * @param type
	 *            The type
	 */
	public void setType(Integer type) {
		this.type = type;
	}

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
	 * @return The message
	 */
	public Double getMessage() {
		return this.message;
	}

	/**
	 *
	 * @param message
	 *            The message
	 */
	public void setMessage(Double message) {
		this.message = message;
	}

	/**
	 *
	 * @return The country
	 */
	public String getCountry() {
		return this.country;
	}

	/**
	 *
	 * @param country
	 *            The country
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 *
	 * @return The sunrise
	 */
	public Integer getSunrise() {
		return this.sunrise;
	}

	/**
	 *
	 * @param sunrise
	 *            The sunrise
	 */
	public void setSunrise(Integer sunrise) {
		this.sunrise = sunrise;
	}

	/**
	 *
	 * @return The sunset
	 */
	public Integer getSunset() {
		return this.sunset;
	}

	/**
	 *
	 * @param sunset
	 *            The sunset
	 */
	public void setSunset(Integer sunset) {
		this.sunset = sunset;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
