# UCS Director Example Spark Plugin
This project intends to be a well documented UCS Director SDK implementation.

It is designed to be easy to consume and full-featured.

## Download & Installation
You can [download it here](https://github.com/CiscoUKIDCDev/ucsd-spark-plugin/releases)

### Spark Developer Key
To install it you will first need a Spark Developer Key.

1. Visit the Cisco Spark Developer community - [https://developer.ciscospark.com/](https://developer.ciscospark.com/)
2. Click Sign In in the top right
3. Click Get Started
4. Under Authentication will be your API key - copy the whole thing
5. For example: ```Bearer XYZwODlmZTQtZTIxYy00MTVjLWExMGEtMDNlYzljMmQyZTgyZTUyMTM4NjctOTI0```

### Installation
Once this is enabled you can install the UCS Director plugin:

1. Under **Administration -> Open Automation** upload the plugin file (**Spark-plugin.zip**). Select it and click **enable**.
2. Once it has finished uploading, select the plugin and click **Enable** to mark it as active
3. ssh to your UCS Director installation as shelladmin and select **3** to stop services and y to confirm
4. Select **4** to start services again

UCS Director will reload. This may take up to 10 minutes.

Once it has come back online, navigate to **Administration -> Physical Accounts**, select the **Physical Accounts** tab and click **Add**.

Select **Storage** as the account type (yes, I know...) and then select **Spark**. You can only add it to a Generic Pod.

## Demo Video
[![Video Demo](http://img.youtube.com/vi/N4l2K2TbfKE/0.jpg)](https://www.youtube.com/watch?v=N4l2K2TbfKE)

You can find [screen shots here](https://github.com/CiscoUKIDCDev/ucsd-spark-plugin/blob/master/screenshots.md)

## Function
This plugin is under active development. New features and screenshots are due
soon.

Right now, it has the following functionality.

### Messages
* Post messages to rooms (GUI or tasks)
* Delete messages (GUI or tasks)
* List messages in a room (via the GUI)
* Rollback (undo) posting a message to a room (tasks)

### Rooms
* Create rooms (GUI or tasks)
* Edit room names (GUI or tasks)
* Delete rooms (GUI or tasks)
* Rollback (undo) creating a room (tasks)

### Members
* Add people to rooms (GUI or tasks)
* Edit if someone is a moderator or not (GUI or tasks)
* Remove people from rooms (GUI or tasks)
* Rollback (undo) adding someone to a room (tasks)

### Teams
* Create teams (GUI or tasks)
* Edit teams names (GUI or tasks)
* Delete teams (GUI or tasks)
* Rollback (undo) creating a team (tasks)

### Team Members
* Add people to teams (GUI or tasks)
* Edit if someone is a team moderator or not (GUI or tasks)
* Remove people from teams (GUI or tasks)
* Rollback (undo) adding someone to a team (tasks)

## License
It is licensed under the Apache 2.0 license (see LICENSE for more information).

