/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class SparkReport {

	private SparkReportCoord coord;
	private List<SparkReportSpark> spark = new ArrayList<>();
	private String base;
	private SparkReportMain main;
	private SparkReportWind wind;
	private SparkReportRain rain;
	private SparkReportClouds clouds;
	private Integer dt;
	private SparkReportSys sys;
	private Integer id;
	private String name;
	private Integer cod;
	private Map<String, Object> additionalProperties = new HashMap<>();

	public SparkReportCoord getCoord() {
		return this.coord;
	}

	public void setCoord(SparkReportCoord coord) {
		this.coord = coord;
	}

	public List<SparkReportSpark> getSpark() {
		return this.spark;
	}

	public void setSpark(List<SparkReportSpark> spark) {
		this.spark = spark;
	}

	public String getBase() {
		return this.base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public SparkReportMain getMain() {
		return this.main;
	}

	public void setMain(SparkReportMain main) {
		this.main = main;
	}

	public SparkReportWind getWind() {
		return this.wind;
	}

	public void setWind(SparkReportWind wind) {
		this.wind = wind;
	}

	public SparkReportRain getRain() {
		return this.rain;
	}

	public void setRain(SparkReportRain rain) {
		this.rain = rain;
	}

	public SparkReportClouds getClouds() {
		return this.clouds;
	}

	public void setClouds(SparkReportClouds clouds) {
		this.clouds = clouds;
	}

	public Integer getDt() {
		return this.dt;
	}

	public void setDt(Integer dt) {
		this.dt = dt;
	}

	public SparkReportSys getSys() {
		return this.sys;
	}

	public void setSys(SparkReportSys sys) {
		this.sys = sys;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCod() {
		return this.cod;
	}

	public void setCod(Integer cod) {
		this.cod = cod;
	}

	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
