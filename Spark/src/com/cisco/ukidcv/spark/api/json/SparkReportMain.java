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
public class SparkReportMain {

	private Double temp;
	private Integer pressure;
	private Integer humidity;
	private Double tempMin;
	private Double tempMax;
	private Map<String, Object> additionalProperties = new HashMap<>();

	/**
	 *
	 * @return The temp
	 */
	public Double getTemp() {
		return this.temp;
	}

	/**
	 *
	 * @param temp
	 *            The temp
	 */
	public void setTemp(Double temp) {
		this.temp = temp;
	}

	/**
	 *
	 * @return The pressure
	 */
	public Integer getPressure() {
		return this.pressure;
	}

	/**
	 *
	 * @param pressure
	 *            The pressure
	 */
	public void setPressure(Integer pressure) {
		this.pressure = pressure;
	}

	/**
	 *
	 * @return The humidity
	 */
	public Integer getHumidity() {
		return this.humidity;
	}

	/**
	 *
	 * @param humidity
	 *            The humidity
	 */
	public void setHumidity(Integer humidity) {
		this.humidity = humidity;
	}

	/**
	 *
	 * @return The tempMin
	 */
	public Double getTempMin() {
		return this.tempMin;
	}

	/**
	 *
	 * @param tempMin
	 *            The temp_min
	 */
	public void setTempMin(Double tempMin) {
		this.tempMin = tempMin;
	}

	/**
	 *
	 * @return The tempMax
	 */
	public Double getTempMax() {
		return this.tempMax;
	}

	/**
	 *
	 * @param tempMax
	 *            The temp_max
	 */
	public void setTempMax(Double tempMax) {
		this.tempMax = tempMax;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
