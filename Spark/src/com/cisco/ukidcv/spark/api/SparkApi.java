/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.api;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.api.json.SparkErrors;
import com.cisco.ukidcv.spark.api.json.SparkMembership;
import com.cisco.ukidcv.spark.api.json.SparkMembershipCreation;
import com.cisco.ukidcv.spark.api.json.SparkMemberships;
import com.cisco.ukidcv.spark.api.json.SparkMessage;
import com.cisco.ukidcv.spark.api.json.SparkMessageFormat;
import com.cisco.ukidcv.spark.api.json.SparkMessageResponse;
import com.cisco.ukidcv.spark.api.json.SparkPersonDetails;
import com.cisco.ukidcv.spark.api.json.SparkRoom;
import com.cisco.ukidcv.spark.api.json.SparkRoomCreation;
import com.cisco.ukidcv.spark.api.json.SparkRoomMessages;
import com.cisco.ukidcv.spark.api.json.SparkRooms;
import com.cisco.ukidcv.spark.api.json.SparkTeam;
import com.cisco.ukidcv.spark.api.json.SparkTeamCreation;
import com.cisco.ukidcv.spark.api.json.SparkTeamMembership;
import com.cisco.ukidcv.spark.api.json.SparkTeamMembershipCreation;
import com.cisco.ukidcv.spark.api.json.SparkTeamMemberships;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.constants.SparkConstants.httpMethod;
import com.cisco.ukidcv.spark.exceptions.SparkAccountException;
import com.cisco.ukidcv.spark.exceptions.SparkReportException;
import com.google.gson.Gson;

/**
 * Spark API requests - obtains raw JSON
 *
 * @author Matt Day
 *
 */
public class SparkApi {
	private static Logger logger = Logger.getLogger(SparkApi.class);

	/**
	 * Requests a list of team memberships from the Spark servers and returns it
	 * in JSON format
	 *
	 * @param account
	 *            Account to request from
	 * @return Membership list in JSON format
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
	 */
	public static String getSparkMemberships(SparkAccount account)
			throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MEMBERSHIP_URI, httpMethod.GET);
		req.execute();
		return req.getResponse();
	}

	/**
	 * Requests a specific member from the Spark servers and returns it as a
	 * Java Object. If the member does not exist (or cannot be found) it returns
	 * null
	 *
	 * @param account
	 *            Account to request from
	 * @param roomId
	 *            Room ID to check
	 * @param personEmail
	 *            User's email to check
	 * @return Membership list in JSON format
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
	 */
	public static String getSparkMemberships(SparkAccount account, String roomId, String personEmail)
			throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_MEMBERSHIP_URI + "?roomId=" + roomId + "&personEmail=" + personEmail,
				httpMethod.GET);
		req.execute();
		String json = req.getResponse();
		Gson gson = new Gson();
		SparkMemberships members = gson.fromJson(json, SparkMemberships.class);
		if (members.getItems().size() > 0) {
			return members.getItems().get(0).getId();
		}
		return null;
	}

	/**
	 * Requests a specific team member from the Spark servers and returns it as
	 * a Java Object. If the team member does not exist (or cannot be found) it
	 * returns null
	 *
	 * @param account
	 *            Account to request from
	 * @param teamId
	 *            Team ID to check
	 * @param personEmail
	 *            User's email to check
	 * @return Membership ID as a String
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
	 */
	public static String getSparkTeamMemberships(SparkAccount account, String teamId, String personEmail)
			throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_TEAMS_MEMBERSHIP_URI + "?teamId=" + teamId + "&personEmail=" + personEmail,
				httpMethod.GET);
		req.execute();
		String json = req.getResponse();
		Gson gson = new Gson();
		SparkTeamMemberships members = gson.fromJson(json, SparkTeamMemberships.class);
		if (members.getItems().size() > 0) {
			// There is a bug (2016-07-15) which means searching via email
			// doesn't work - find it ourselves
			for (SparkTeamMembership member : members.getItems()) {
				if (personEmail.toLowerCase().equals(member.getPersonEmail().toLowerCase())) {
					return member.getId();
				}
			}
		}
		return null;
	}

	/**
	 * Requests a list of room memberships from the Spark servers and returns it
	 * as a Java class.
	 * <p>
	 * <b>Caution:</b>This method is not cached!
	 *
	 * @param account
	 *            Account to request from
	 * @param roomId
	 *            Room ID
	 * @return SparkMemberships
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkMemberships
	 */
	public static SparkMemberships getSparkRoomMemberships(SparkAccount account, String roomId)
			throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_MEMBERSHIP_URI + "?roomId=" + roomId, httpMethod.GET);
		req.execute();
		Gson gson = new Gson();
		return gson.fromJson(req.getResponse(), SparkMemberships.class);
	}

	/**
	 * Get Spark room details from a Spark Membership ID
	 *
	 * @param account
	 *            Account to check from
	 * @param memberId
	 *            Member ID to check
	 * @return Membership information
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static SparkMembership getSparkRoomDetails(SparkAccount account, String memberId)
			throws ClientProtocolException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MEMBERSHIP_URI + "/" + memberId,
				httpMethod.GET);
		req.execute();
		Gson gson = new Gson();
		return gson.fromJson(req.getResponse(), SparkMembership.class);
	}

	/**
	 * Get Spark team details from a Spark Membership ID
	 *
	 * @param account
	 *            Account to check from
	 * @param memberId
	 *            Member ID to check
	 * @return Membership information
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static SparkTeamMembership getSparkTeamDetails(SparkAccount account, String memberId)
			throws ClientProtocolException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_TEAMS_MEMBERSHIP_URI + "/" + memberId, httpMethod.GET);
		req.execute();
		Gson gson = new Gson();
		return gson.fromJson(req.getResponse(), SparkTeamMembership.class);
	}

	/**
	 * Creates a new membership using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param roomId
	 *            Room ID to change
	 * @param personEmail
	 *            Person to add
	 * @param moderator
	 *            true if this person should be a moderator
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus createMembership(SparkAccount account, String roomId, String personEmail,
			boolean moderator) throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MEMBERSHIP_URI,
				httpMethod.POST);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkMembershipCreation(roomId, personEmail, moderator));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage(), req.getResponse());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null, req.getResponse());
	}

	/**
	 * Takes a Spark membership response (typically via a SparkApiStatus
	 * response) and parses the responmse
	 *
	 * @param json
	 *            JSON response from a createMember
	 * @return SparkMembership
	 * @see SparkMembership
	 * @see #createMembership
	 */
	public static SparkMembership getMembershipResponse(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SparkMembership.class);
	}

	/**
	 * Updates a membership using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param membershipId
	 *            Membership ID to change
	 * @param moderator
	 *            Change if they should be a moderator or not
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus updateMembership(SparkAccount account, String membershipId, boolean moderator)
			throws ClientProtocolException, IOException {
		// Create a new PUT request
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_MEMBERSHIP_URI + "/" + membershipId, httpMethod.PUT);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkMembershipCreation(moderator));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage(), json);
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null, json);
	}

	/**
	 * Delete a Spark membership using the Spark API
	 *
	 * @param account
	 *            Account to delete from
	 * @param memberId
	 *            Room ID to delete
	 * @return Status of request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static SparkApiStatus deleteMembership(SparkAccount account, String memberId)
			throws ClientProtocolException, IOException {
		// Create a new DELETE request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MEMBERSHIP_URI + "/" + memberId,
				httpMethod.DELETE);

		req.execute();

		// If the request does not return 204 (deleted) it's an error, return
		// details:
		if (req.getCode() != 204) {
			Gson gson = new Gson();
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Requests a list of teams from the Spark servers and returns it in JSON
	 * format
	 *
	 * @param account
	 *            Account to request from
	 * @return Team list in JSON format
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
	 */
	public static String getSparkTeams(SparkAccount account) throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_TEAMS_URI, httpMethod.GET);
		req.execute();
		return req.getResponse();
	}

	/**
	 * Requests a list of team memberships from the Spark servers and returns it
	 * as a Java class.
	 * <p>
	 * <b>Caution:</b>This method is not cached!
	 *
	 * @param account
	 *            Account to request from
	 * @param teamId
	 *            Team ID
	 * @return SparkMemberships
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkMemberships
	 */
	public static SparkTeamMemberships getSparkTeamMemberships(SparkAccount account, String teamId)
			throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_TEAMS_MEMBERSHIP_URI + "?teamId=" + teamId, httpMethod.GET);
		req.execute();
		Gson gson = new Gson();
		return gson.fromJson(req.getResponse(), SparkTeamMemberships.class);
	}

	/**
	 * Creates a new team using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param teamName
	 *            New team name
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus createTeam(SparkAccount account, String teamName)
			throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_TEAMS_URI, httpMethod.POST);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkTeamCreation(teamName));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = null;
			try {
				error = gson.fromJson(req.getResponse(), SparkErrors.class);
				return new SparkApiStatus(false, error.getMessage());
			}
			catch (Exception e) {
				logger.error("Could not create team " + e.getMessage());
				return new SparkApiStatus(false, e.getMessage());
			}
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null, req.getResponse());
	}

	/**
	 * Takes a Spark team response (typically via a SparkApiStatus response) and
	 * parses the responmse
	 *
	 * @param json
	 *            JSON response from a createMember
	 * @return SparkTeam
	 * @see SparkTeam
	 * @see #createTeam
	 */
	public static SparkTeam getTeamResponse(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SparkTeam.class);
	}

	/**
	 * Updates team using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param teamId
	 *            Team ID to change
	 * @param newName
	 *            New room name
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus updateTeam(SparkAccount account, String teamId, String newName)
			throws ClientProtocolException, IOException {
		// Create a new PUT request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_TEAMS_URI + "/" + teamId,
				httpMethod.PUT);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkTeamCreation(newName));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Delete a Spark team using the Spark API
	 *
	 * @param account
	 *            Account to delete from
	 * @param teamId
	 *            Team ID to delete
	 * @return Status of request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static SparkApiStatus deleteTeam(SparkAccount account, String teamId)
			throws ClientProtocolException, IOException {
		// Create a new DELETE request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_TEAMS_URI + "/" + teamId,
				httpMethod.DELETE);

		req.execute();

		// If the request does not return 204 (deleted) it's an error, return
		// details:
		if (req.getCode() != 204) {
			Gson gson = new Gson();
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Creates a new team membership using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param teamId
	 *            Room ID to change
	 * @param personEmail
	 *            Person to add
	 * @param moderator
	 *            true if this person should be a moderator
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus createTeamMembership(SparkAccount account, String teamId, String personEmail,
			boolean moderator) throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_TEAMS_MEMBERSHIP_URI,
				httpMethod.POST);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkTeamMembershipCreation(teamId, personEmail, moderator));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Updates a team membership using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param membershipId
	 *            Membership ID to change
	 * @param moderator
	 *            Change if they should be a moderator or not
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus updateTeamMembership(SparkAccount account, String membershipId, boolean moderator)
			throws ClientProtocolException, IOException {
		// Create a new PUT request
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_TEAMS_MEMBERSHIP_URI + "/" + membershipId, httpMethod.PUT);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkTeamMembershipCreation(moderator));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Delete a Spark team membership using the Spark API
	 *
	 * @param account
	 *            Account to delete from
	 * @param memberId
	 *            Team Member ID to delete
	 * @return Status of request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static SparkApiStatus deleteTeamMembership(SparkAccount account, String memberId)
			throws ClientProtocolException, IOException {
		// Create a new DELETE request
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_TEAMS_MEMBERSHIP_URI + "/" + memberId, httpMethod.DELETE);

		req.execute();

		// If the request does not return 204 (deleted) it's an error, return
		// details:
		if (req.getCode() != 204) {
			Gson gson = new Gson();
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Requests a list of rooms from the Spark servers and returns it in JSON
	 * format
	 *
	 * @param account
	 *            Account to request from
	 * @return List of rooms in JSON format
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
	 */
	public static String getSparkRooms(SparkAccount account) throws SparkReportException, HttpException, IOException {
		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ROOM_URI, httpMethod.GET);
		req.execute();
		return req.getResponse();
	}

	/**
	 * Creates a new room using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param roomName
	 *            New room name
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus createRoom(SparkAccount account, String roomName)
			throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ROOM_URI, httpMethod.POST);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkRoomCreation(roomName));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null, req.getResponse());
	}

	/**
	 * Creates a new room using the Spark API and associates it with a specific
	 * team
	 *
	 * @param account
	 *            Account to create it from
	 * @param roomName
	 *            New room name
	 * @param teamId
	 *            Team to add the user to
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus createRoom(SparkAccount account, String roomName, String teamId)
			throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ROOM_URI, httpMethod.POST);

		SparkRoomCreation room = new SparkRoomCreation(roomName);
		room.setTeamId(teamId);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(room);

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null, req.getResponse());
	}

	/**
	 * Takes a Spark room response (typically via a SparkApiStatus response) and
	 * parses the responmse
	 *
	 * @param json
	 *            JSON response from a createRoom
	 * @return SparkRoom
	 * @see SparkRoom
	 * @see #createRoom
	 */
	public static SparkRoom getRoomResponse(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SparkRoom.class);
	}

	/**
	 * Updates room using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param roomId
	 *            Room ID to change
	 * @param newName
	 *            New room name
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus updateRoom(SparkAccount account, String roomId, String newName)
			throws ClientProtocolException, IOException {
		// Create a new PUT request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ROOM_URI + "/" + roomId,
				httpMethod.PUT);

		Gson gson = new Gson();

		// Create JSON room request:
		String json = gson.toJson(new SparkRoomCreation(newName));

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Delete a Spark room using the Spark API
	 *
	 * @param account
	 *            Account to delete from
	 * @param roomId
	 *            Room ID to delete
	 * @return Status of request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static SparkApiStatus deleteRoom(SparkAccount account, String roomId)
			throws ClientProtocolException, IOException {
		// Create a new DELETE request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ROOM_URI + "/" + roomId,
				httpMethod.DELETE);

		req.execute();

		// If the request does not return 204 (deleted) it's an error, return
		// details:
		if (req.getCode() != 204) {
			Gson gson = new Gson();
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Sends a new message to a room using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param roomId
	 *            Room to send the message
	 * @param message
	 *            Message to send
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus sendMessageToRoom(SparkAccount account, String roomId, SparkMessage message)
			throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MESSAGES_URI, httpMethod.POST);

		Gson gson = new Gson();

		SparkMessageFormat newMessage = new SparkMessageFormat(message);

		newMessage.setRoomId(roomId);

		// Create JSON room request:
		String json = gson.toJson(newMessage);

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage(), null);
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null, req.getResponse());
	}

	/**
	 * Takes a Spark message response (typically via a SparkApiStatus response)
	 * and parses the responmse
	 *
	 * @param json
	 *            JSON response from a sendMessageToRoom
	 * @return SparkMessageResponse
	 * @see SparkMessageResponse
	 * @see #sendMessageToRoom
	 */
	public static SparkMessageResponse getMessageResponse(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, SparkMessageResponse.class);
	}

	/**
	 * Sends a new message to a person using the Spark API
	 *
	 * @param account
	 *            Account to create it from
	 * @param emailAddress
	 *            Email address to send the message
	 * @param message
	 *            Message to send
	 * @return Status of request
	 *
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static SparkApiStatus sendMessageToPerson(SparkAccount account, String emailAddress, SparkMessage message)
			throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MESSAGES_URI, httpMethod.POST);

		Gson gson = new Gson();

		SparkMessageFormat newMessage = new SparkMessageFormat(message);

		newMessage.setToPersonEmail(emailAddress);

		// Create JSON room request:
		String json = gson.toJson(newMessage);

		// Set it to the request body (must be POST or PUT)
		req.setBody(json);
		req.execute();

		// If the request does not return 200 (success) it's an error, return
		// details:
		if (req.getCode() != 200) {
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Delete a Spark message using the Spark API
	 *
	 * @param account
	 *            Account to delete from
	 * @param messageId
	 *            Message to delete
	 * @return Status of request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static SparkApiStatus deleteMessage(SparkAccount account, String messageId)
			throws ClientProtocolException, IOException {
		// Create a new DELETE request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MESSAGES_URI + "/" + messageId,
				httpMethod.DELETE);

		req.execute();

		// If the request does not return 204 (deleted) it's an error, return
		// details:
		if (req.getCode() != 204) {
			Gson gson = new Gson();
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkApiStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkApiStatus(true, null);
	}

	/**
	 * Returns a list of messages for an account
	 *
	 * @param account
	 *            Account to check from
	 * @param max
	 *            Maximum number of messages to return
	 * @return Specified spark messages
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkRoomMessages getMessages(SparkAccount account, int max)
			throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MESSAGES_URI + "&max=" + max,
				httpMethod.GET);
		req.execute();
		Gson gson = new Gson();
		return gson.fromJson(req.getResponse(), SparkRoomMessages.class);
	}

	/**
	 * Returns a list of messages for an account
	 *
	 * @param account
	 *            Account to check from
	 * @return Specified spark messages
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkRoomMessages getMessages(SparkAccount account)
			throws SparkReportException, HttpException, IOException {
		// Use default maximum messages
		return getMessages(account, SparkConstants.SPARK_MAXIMUM_MESSAGES);
	}

	/**
	 * Returns a list of messages from a spark room
	 *
	 * @param account
	 *            Account to check from
	 * @param roomId
	 *            Room ID to get messages from
	 * @param max
	 *            Maximum number of messages to return
	 * @return Specified spark messages
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkRoomMessages getMessages(SparkAccount account, String roomId, int max)
			throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account,
				SparkConstants.SPARK_MESSAGES_URI + "?roomId=" + roomId + "&max=" + max, httpMethod.GET);
		req.execute();
		Gson gson = new Gson();
		return gson.fromJson(req.getResponse(), SparkRoomMessages.class);
	}

	/**
	 * Returns a list of messages from a spark room
	 *
	 * @param account
	 *            Account to check from
	 * @param roomId
	 *            Room ID to get messages from
	 * @return Specified spark messages
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static SparkRoomMessages getMessages(SparkAccount account, String roomId)
			throws SparkReportException, HttpException, IOException {
		// Use default maximum messages
		return getMessages(account, roomId, SparkConstants.SPARK_MAXIMUM_MESSAGES);
	}

	/**
	 * Returns JSON information on the signed-in user
	 *
	 * @param account
	 *            Account to check from
	 * @return JSON formatted response from the server
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static String getSparkPerson(SparkAccount account) throws SparkReportException, HttpException, IOException {

		// Set up a request to the spark server
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_ME_URI, httpMethod.GET);
		req.execute();
		return req.getResponse();
	}

	/**
	 * Returns information on a specific user ID
	 *
	 * @param account
	 *            Account to check from
	 * @param userId
	 *            User ID to check
	 * @return JSON formatted response from the server
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 */
	public static String getSparkPerson(SparkAccount account, String userId)
			throws SparkReportException, HttpException, IOException {

		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_PEOPLE_URI + userId,
				httpMethod.GET);
		req.execute();
		return req.getResponse();
	}

	/**
	 * Tests if an account can correctly connect to Spark
	 *
	 * @param account
	 *            Account to test
	 * @return true if the connection is successful
	 * @throws SparkAccountException
	 *             if there's a problem
	 */
	public static boolean testConnection(SparkAccount account) throws SparkAccountException {
		try {
			String json = getSparkPerson(account);
			// Check if the response is not empty:
			if (!"".equals(json)) {
				Gson gson = new Gson();
				SparkPersonDetails person = gson.fromJson(json, SparkPersonDetails.class);
				if ((person.getId() != null) && (!"".equals(person.getId()))) {
					return true;
				}
			}
		}
		catch (SparkReportException | IOException e) {
			logger.warn("Connection test failed: " + e.getMessage());
			throw new SparkAccountException("Connection test failed: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Perform a full inventory collection after any changes made
	 *
	 * @param account
	 *            Account to perform inventory from
	 */
	private static void updateInventory(SparkAccount account) {

		try {
			SparkInventory.update(account, SparkConstants.INVENTORY_REASON_CRUD, true);
		}
		catch (@SuppressWarnings("unused") Exception e) {
			// Do nothing
		}
	}

}
