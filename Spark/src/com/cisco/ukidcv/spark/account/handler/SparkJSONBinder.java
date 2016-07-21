/*******************************************************************************
 * Copyright (c) 2016 Cisco and/or its affiliates
 * @author Matt Day
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.handler;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.cloupia.service.cIM.inframgr.collector.controller.ItemDataObjectBinderIf;

/**
 * Boilerplate from SDK, not sure what it does!
 *
 * @author Matt Day
 *
 */
public abstract class SparkJSONBinder implements ItemDataObjectBinderIf {

	@SuppressWarnings({
			"rawtypes", "javadoc"
	})
	public abstract List<Class> getPersistantClassList();

	@SuppressWarnings("static-method")
	protected void bindContext(Object obj, Map<String, Object> context) {
		for (final Map.Entry<String, Object> entry : context.entrySet()) {
			final String varName = entry.getKey();
			final Object value = entry.getValue();
			try {
				final Field field = obj.getClass().getDeclaredField(varName);
				field.setAccessible(true);
				if (value != null) {
					field.set(obj, value);
				}
			}
			catch (@SuppressWarnings("unused") Exception e) {
				// Do nothing
			}
		}
	}
}
