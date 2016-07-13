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
import com.cisco.ukidcv.spark.api.json.SparkMembershipCreation;
import com.cisco.ukidcv.spark.api.json.SparkMemberships;
import com.cisco.ukidcv.spark.api.json.SparkMessage;
import com.cisco.ukidcv.spark.api.json.SparkMessageCreation;
import com.cisco.ukidcv.spark.api.json.SparkPersonDetails;
import com.cisco.ukidcv.spark.api.json.SparkRoomCreation;
import com.cisco.ukidcv.spark.api.json.SparkRooms;
import com.cisco.ukidcv.spark.api.json.SparkTeamCreation;
import com.cisco.ukidcv.spark.api.json.SparkTeamMembershipCreation;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.constants.SparkConstants.httpMethod;
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
	 * Requests a list of room memberships from the Spark servers and returns it
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
	 * Requests a list of room memberships from the Spark servers and returns it
	 * as a Java class.
	 * <p>
	 * <b>Caution:</b>This method is not cached!
	 *
	 * @param account
	 *            Account to request from
	 * @param roomId
	 *            Room ID
	 * @return Membership list in JSON format
	 * @throws SparkReportException
	 *             if the report fails
	 * @throws HttpException
	 *             if there's a problem accessing the report
	 * @throws IOException
	 *             if there's a problem accessing the report
	 * @see SparkRooms
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
	public static SparkAPIStatus createMembership(SparkAccount account, String roomId, String personEmail,
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus updateMembership(SparkAccount account, String membershipId, boolean moderator)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus deleteMembership(SparkAccount account, String memberId)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus createTeam(SparkAccount account, String teamName)
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
			SparkErrors error = gson.fromJson(req.getResponse(), SparkErrors.class);
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus updateTeam(SparkAccount account, String teamId, String newName)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus deleteTeam(SparkAccount account, String teamId)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus createTeamMembership(SparkAccount account, String teamId, String personEmail,
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus updateTeamMembership(SparkAccount account, String membershipId, boolean moderator)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus deleteTeamMembership(SparkAccount account, String memberId)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus createRoom(SparkAccount account, String roomName)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus updateRoom(SparkAccount account, String roomId, String newName)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus deleteRoom(SparkAccount account, String roomId)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus sendMessageToRoom(SparkAccount account, String roomId, SparkMessage message)
			throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MESSAGES_URI, httpMethod.POST);

		Gson gson = new Gson();

		SparkMessageCreation newMessage = new SparkMessageCreation(message);

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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus sendMessageToPerson(SparkAccount account, String emailAddress, SparkMessage message)
			throws ClientProtocolException, IOException {
		// Create a new POST request
		SparkHttpConnection req = new SparkHttpConnection(account, SparkConstants.SPARK_MESSAGES_URI, httpMethod.POST);

		Gson gson = new Gson();

		SparkMessageCreation newMessage = new SparkMessageCreation(message);

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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	public static SparkAPIStatus deleteMessage(SparkAccount account, String messageId)
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
			return new SparkAPIStatus(false, error.getMessage());
		}
		// Update inventory after this operation
		updateInventory(account);
		return new SparkAPIStatus(true, null);
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
	 */
	public static boolean testConnection(SparkAccount account) {
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
