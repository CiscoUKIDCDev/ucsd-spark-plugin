/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api.json;

import java.util.ArrayList;
import java.util.List;

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
public class SparkRoomMessages {
	private List<SparkMessageFormat> items = new ArrayList<>();

	public List<SparkMessageFormat> getItems() {
		return this.items;
	}

	public void setItems(List<SparkMessageFormat> items) {
		this.items = items;
	}

}
