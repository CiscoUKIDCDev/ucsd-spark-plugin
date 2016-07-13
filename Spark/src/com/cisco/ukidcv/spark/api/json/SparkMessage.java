/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Means to create a Spark message
 * <p>
 * A spark message can consist of Markdown, plain text and images. This lets you
 * define all, some, or none of them at once!
 *
 * @author Matt Day
 *
 */
public class SparkMessage {
	private String text = null;
	private String markdown = null;
	private List<String> files = new ArrayList<>(1);
	private boolean hasText = false;
	private boolean hasMarkdown = false;
	private boolean hasFiles = false;

	/**
	 * Initialise with plain old text
	 *
	 * @param text
	 */
	public SparkMessage(String text) {
		this.setText(text);
	}

	/**
	 * Initialise with nothing. You will need to add text, markdown or files.
	 */
	public SparkMessage() {
		super();
	}

	/**
	 *
	 * @return Text message
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * Sets a text message
	 *
	 * @param text
	 *            Plain text
	 */
	public void setText(String text) {
		this.hasText = true;
		this.text = text;
	}

	/**
	 *
	 * @return Markdown message
	 */
	public String getMarkdown() {
		return this.markdown;
	}

	/**
	 * Sets a markdown message
	 *
	 * @param markdown
	 *            Markdown formatted text
	 */
	public void setMarkdown(String markdown) {
		this.hasMarkdown = true;
		this.markdown = markdown;
	}

	/**
	 *
	 * @return Returns list of all files
	 */
	public List<String> getFiles() {
		return this.files;
	}

	/**
	 * Adds a file to be shared
	 *
	 * @param file
	 *            URL of file location
	 */
	public void addFiles(String file) {
		this.hasFiles = true;
		this.files.add(file);
	}

	/**
	 *
	 * @return true if text is set
	 */
	public boolean hasText() {
		return this.hasText;
	}

	/**
	 *
	 * @return true if markdown is set
	 */
	public boolean hasMarkdown() {
		return this.hasMarkdown;
	}

	/**
	 *
	 * @return true if any files are set
	 */
	public boolean hasFiles() {
		return this.hasFiles;
	}

}
