/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api.json;

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
public class SparkMessageCreation {

	public String roomId;
	public String toPersonId;
	public String toPersonEmail;
	public String text;
	public String markdown;
	public List<String> files;

	public SparkMessageCreation(SparkMessage message) {
		if (message.hasText()) {
			this.setText(message.getText());
		}
		if (message.hasFiles()) {
			this.setFiles(message.getFiles());
		}
		if (message.hasMarkdown()) {
			this.setMarkdown(message.getMarkdown());
		}
	}

	public SparkMessageCreation() {

	}

	public String getRoomId() {
		return this.roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getToPersonId() {
		return this.toPersonId;
	}

	public void setToPersonId(String toPersonId) {
		this.toPersonId = toPersonId;
	}

	public String getToPersonEmail() {
		return this.toPersonEmail;
	}

	public void setToPersonEmail(String toPersonEmail) {
		this.toPersonEmail = toPersonEmail;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getMarkdown() {
		return this.markdown;
	}

	public void setMarkdown(String markdown) {
		this.markdown = markdown;
	}

	public List<String> getFiles() {
		return this.files;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

}