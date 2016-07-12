/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.inventory;

import java.util.LinkedList;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.apache.log4j.Logger;

/**
 * Provides the JDO notation to cache entries to the UCS Director database.
 * <p>
 * The JSON format here is exactly the same as the Spark API - we're just using
 * it to cache entries
 *
 * @author Matt Day
 *
 */
@PersistenceCapable(detachable = "true", table = "spark_inventory_db")
public class SparkInventoryDB {

	private static Logger logger = Logger.getLogger(SparkInventoryDB.class);

	// ID for this entry in the database (make it primary key)
	@PrimaryKey
	@Column(name = "ID")
	private long id;

	// Account name this pertains to
	@Persistent
	private String accountName;

	// When it was last updated as an epoch value
	@Persistent
	private long updated;

	// Log file of historical polling
	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private LinkedList<String> polling;

	// CLOB is an SQL data type of unlimited length strings; perfect for JSON
	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String roomList;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String membershipList;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String teamList;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String teamMembershipList;

	@Persistent(defaultFetchGroup = "true")
	@Column(jdbcType = "CLOB")
	private String me;

	/**
	 * Initialise inventory with an account name
	 *
	 * @param accountName
	 *            Name of the account to persist
	 */
	public SparkInventoryDB(String accountName) {
		this.accountName = accountName;
		logger.info("Created persistent entry for account " + accountName);

		// Populate all fields

		try {

			logger.info("Setting up polling history");
			this.polling = new LinkedList<>();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the room list in JSON format
	 *
	 * @return room list in JSON format
	 */
	public String getRoomList() {
		return this.roomList;
	}

	/**
	 * Sets the room list in JSON format
	 *
	 * @param roomList
	 *            List of rooms in JSON format
	 */
	public void setRoomList(String roomList) {
		this.roomList = roomList;
	}

	/**
	 * Gets the membership list in JSON format
	 *
	 * @return Membership list in JSON format
	 */
	public String getMembershipList() {
		return this.membershipList;
	}

	/**
	 * Sets the membership list in JSON format
	 *
	 * @param membershipList
	 *            List of members in JSON format
	 */
	public void setMembershipList(String membershipList) {
		this.membershipList = membershipList;
	}

	/**
	 * Gets the list of teams in JSON format
	 *
	 * @return Team list in JSON format
	 */
	public String getTeamList() {
		return this.teamList;
	}

	/**
	 * Sets the team list
	 *
	 * @param teamList
	 *            Team list in JSON format
	 */
	public void setTeamList(String teamList) {
		this.teamList = teamList;
	}

	/**
	 * Gets the list of team members
	 *
	 * @return List of team members in JSON format
	 */
	public String getTeamMembershipList() {
		return this.teamMembershipList;
	}

	/**
	 * Sets the team membership list in JSON format
	 *
	 * @param teamMembershipList
	 *            list of team members in JSON format
	 */
	public void setTeamMembershipList(String teamMembershipList) {
		this.teamMembershipList = teamMembershipList;
	}

	/**
	 * Gets information about the logged-in user
	 *
	 * @return Information about the logged in user
	 */
	public String getMe() {
		return this.me;
	}

	/**
	 * Sets information about the logged in user
	 *
	 * @param me
	 *            Information about the user in JSON format
	 */
	public void setMe(String me) {
		this.me = me;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public long getUpdated() {
		return this.updated;
	}

	public void setUpdated(long updated) {
		this.updated = updated;
	}

	public LinkedList<String> getPolling() {
		return this.polling;
	}

	public void setPolling(LinkedList<String> polling) {
		this.polling = polling;
	}

}
