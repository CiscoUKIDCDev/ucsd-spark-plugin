/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.constants;

/**
 * Stores various constants used throughout the code
 *
 * @author Matt Day
 *
 */
public class SparkConstants {

	// ======== UCSD Account Constants
	/**
	 * UCSD Internal account type. Should NOT be changed between releases or it
	 * breaks all kinds of things
	 */
	public static final String INFRA_ACCOUNT_TYPE = "SPARK";
	/**
	 * User-friendly label for this account type. This can be changed between
	 * releases.
	 */
	public static final String INFRA_ACCOUNT_LABEL = "Spark";
	/**
	 * Accounts must have a magic number in the converged view. The docs say to
	 * use something "over 1000". Let's hope no one else uses this value!
	 */
	public static final int INFRA_ACCOUNT_MAGIC_NUMBER = 19852807;
	/**
	 * Category to put all the workflows
	 */
	public static final String WORKFLOW_CATEGORY = "Cisco Spark";
	/**
	 * Folder to put the tasks in
	 */
	public static final String TASK_PREFIX = "Cisco Spark Tasks";

	// ======== Spark GUI Constants
	/**
	 * Label for summary reports
	 */
	public static final String SUMMARY_LABEL = "Summary";
	/**
	 * Label for overview reports
	 */
	public static final String OVERVIEW_LABEL = "Overview";
	/**
	 * Label for overview reports
	 */
	public static final String ROOM_LABEL = "Rooms";
	/**
	 * Label for room member reports
	 */
	public static final String ROOM_MEMBERS_LABEL = "Room Members";
	/**
	 * Label for recent messages
	 */
	public static final String ROOM_RECENT_MESSAGES_LABEL = "Recent Messages";
	/**
	 * Label for overview reports
	 */
	public static final String INVENTORY_LABEL = "Polling";
	/**
	 * Label for overview reports
	 */
	public static final String ROOM_TYPE_LABEL = "Room Types";
	/**
	 * Label for room name
	 */
	public static final String ROOM_NAME_LABEL = "Room Name";
	/**
	 * Label for new room name
	 */
	public static final String NEW_ROOM_NAME_LABEL = "New Name";
	/**
	 * Label for Email address
	 */
	public static final String EMAIL_LABEL = "Email Address";
	/**
	 * Label for Email address
	 */
	public static final String MEMBERSHIP_ID_LABEL = "Membership ID";

	/**
	 * Label for new message
	 */
	public static final String MESSAGE_TEXT_LABEL = "Message";
	/**
	 * Label for file URL
	 */
	public static final String MESSAGE_FILE_URL_LABEL = "File URL";

	/**
	 * Label for Moderator
	 */
	public static final String MODERATOR_LABEL = "Moderator";

	/**
	 * Label for Spark Account Tasks
	 */
	public static final String ACCOUNT_LIST_FORM_LABEL = "Spark Account";
	/**
	 * Label for inventory task
	 */
	public static final String INVENTORY_TASK_LABEL = "Collect Spark Inventory";
	/**
	 * Label for create room task
	 */
	public static final String CREATE_ROOM_TASK_LABEL = "Create Room";
	/**
	 * Label for edit room task
	 */
	public static final String EDIT_ROOM_TASK_LABEL = "Edit Room";
	/**
	 * Label for edit membership task
	 */
	public static final String EDIT_MEMBERSHIP_TASK_LABEL = "Edit Member";
	/**
	 * Label for edit membership task
	 */
	public static final String DELETE_MEMBERSHIP_TASK_LABEL = "Remove Member";
	/**
	 * Label for create room task
	 */
	public static final String ADD_MEMBERSHIP_TASK_LABEL = "Add Member";
	/**
	 * Label for edit room task
	 */
	public static final String DELETE_ROOM_TASK_LABEL = "Delete Room";

	/**
	 * Label for the post message task
	 */
	public static final String POST_MESSAGE_TASK_LABEL = "Send Message";
	// ======== Report Constants
	/**
	 * Context for room drilldowns
	 */
	public static final String ROOM_LIST_DRILLDOWN = "com.cisco.ukidcv.spark.reports.rooms.SparkRoomList";
	/**
	 * Label for room drilldowns
	 */
	public static final String ROOM_LIST_DRILLDOWN_LABEL = "Spark Room List";
	/**
	 * Context for room drilldowns
	 */
	public static final String INVENTORY_LIST_DRILLDOWN = "com.cisco.ukidcv.spark.reports.inventory.SparkInventoryList";
	/**
	 * Label for room drilldowns
	 */
	public static final String INVENTORY_LIST_DRILLDOWN_LABEL = "Spark Inventory List";
	/**
	 * Account list UCS Director internal name
	 */
	public static final String ACCOUNT_LIST_FORM_PROVIDER = "spark_account_list_provider";

	/**
	 * Account list data type name
	 */
	public static final String ACCOUNT_LIST_FORM_TABLE_NAME = "spark_account_list_table";

	/**
	 * Account list form name
	 */
	public static final String ACCOUNT_LIST_FORM_NAME = "SparkAccountList";

	/**
	 * Label for message ID
	 */
	public static final String MESSAGE_LIST_FORM_LABEL = "Message ID";

	/**
	 * Message list UCS Director internal name
	 */
	public static final String MESSAGE_LIST_FORM_PROVIDER = "spark_message_list_provider";

	/**
	 * Label for message ID
	 */
	public static final String MESSAGE_LIST_FORM_TABLE_NAME = "spark_message_list_table";

	/**
	 * Account list form name
	 */
	public static final String MESSAGE_LIST_FORM_NAME = "SparkMessageList";

	/**
	 * Room list UCS Director internal name
	 */
	public static final String ROOM_LIST_FORM_PROVIDER = "spark_room_list_provider";

	/**
	 * Room list data type name
	 */
	public static final String ROOM_LIST_FORM_TABLE_NAME = "spark_room_list_table";

	/**
	 * Room list form name
	 */
	public static final String ROOM_LIST_FORM_NAME = "SparkRoomList";

	/**
	 * UCSDs internal gen_text_input type
	 */
	public static final String GENERIC_TEXT_INPUT = "gen_text_input";

	/**
	 * UCSDs internal boolean type
	 */
	public static final String BOOLEAN_INPUT = "Boolean";

	// ======== Spark API Constants
	/**
	 * Enum type for http method
	 */
	public enum httpMethod {
		/**
		 * http POST method
		 */
		POST,
		/**
		 * http GET method
		 */
		GET,
		/**
		 * http DELETE method
		 */
		DELETE,
		/**
		 * http PUT method
		 */
		PUT
	}

	/**
	 * API Version
	 */
	public static final String API_VERSION = "1";
	/**
	 * Spark server hostname
	 */
	public static final String SPARK_SERVER_HOSTNAME = "api.ciscospark.com";
	/**
	 * Spark server port
	 */
	public static final int SPARK_SERVER_PORT = 443;
	/**
	 * Maximum number of messages to show in a messages report
	 */
	public static final int SPARK_MAXIMUM_MESSAGES = 50;

	/**
	 * Spark server protocol
	 */
	public static final String SPARK_SERVER_PROTOCOL = "https";

	/**
	 * URI for room report
	 */
	public final static String SPARK_ROOM_URI = "/v1/rooms";

	/**
	 * URI for membership report
	 */
	public final static String SPARK_MEMBERSHIP_URI = "/v1/memberships";

	/**
	 * URI for messages
	 */
	public final static String SPARK_MESSAGES_URI = "/v1/messages";

	/**
	 * URI teams report
	 */
	public final static String SPARK_TEAMS_URI = "/v1/teams";

	/**
	 * URI teams membership
	 */
	public final static String SPARK_TEAMS_MEMBERSHIP_URI = "/v1/team/memberships";

	/**
	 * URI for people report
	 */
	public final static String SPARK_PEOPLE_URI = "/v1/people/";
	/**
	 * URI for 'me' report
	 */
	public final static String SPARK_ME_URI = "/v1/people/me";

	// ======== Spark Inventory constants
	/**
	 * Log message for periodic inventory updates
	 */
	public final static String INVENTORY_REASON_PERIODIC = "Periodic inventory update";
	/**
	 * Log message for initial startup
	 */
	public final static String INVENTORY_REASON_INITIAL = "Initial inventory collection";
	/**
	 * Log message for initial startup
	 */
	public final static String INVENTORY_REASON_USER = "User requested collection";

	/**
	 * Log message for initial startup
	 */
	public final static String INVENTORY_REASON_CRUD = "Performed a Spark operation";

	/**
	 * Maximum number of log entries
	 */
	public final static int MAX_POLLING_LOG_ENTRIES = 100;

	/**
	 * Time between polling (miliseconds)
	 */
	public final static long MAX_POLLING_TIME = 9000000;

}
