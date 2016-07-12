/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.cloupia.service.cIM.inframgr.collector.model.ItemResponse;

/**
 * Boilerplate from SDK, not sure what it does!
 *
 * @author Matt Day
 *
 */
public class SparkAccountJSONBinder extends SparkJSONBinder {
	private static Logger logger = Logger.getLogger(SparkAccountJSONBinder.class);

	@Override
	public ItemResponse bind(ItemResponse bindable) {
		String jsonData = bindable.getCollectedData();
		logger.debug("RAW JSON Data" + jsonData);

		if ((jsonData != null) && !jsonData.isEmpty()) {
			// Json data to be converted as target object

		}

		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Class> getPersistantClassList() {
		List<Class> cList = new ArrayList<>();
		// add the Persistant class in the CList , for reference.
		return cList;
	}
}
