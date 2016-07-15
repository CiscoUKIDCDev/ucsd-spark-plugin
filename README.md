# UCS Director Example Spark Plugin
This project intends to be a well documented UCS Director SDK implementation.

It is designed to be easy to consume and full-featured.

## Download & Installation
You can [download it here](https://github.com/CiscoUKIDCDev/ucsd-spark-plugin/releases)

### Installation
1. Under **Administration -> Open Automation** upload the plugin file (Spark-plugin.zip). Select it and click **enable**.
2. From the CLI (typically ssh logged in as shelladmin) select **3** to stop services and then **4** to start them.
3. You can then add a Spark account under a Generic pod (it's confusingly a **storage** account!)

You can then administer your Spark account(s) either via custom workflow tasks, or via the UCS Director GUI.

## Demo Video
[![Video Demo](http://img.youtube.com/vi/N4l2K2TbfKE/0.jpg)](https://www.youtube.com/watch?v=N4l2K2TbfKE)

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

## Intended features
* Adding and managing accounts
* Full reports, including:
	* Summary
	* Drilldowns
	* Graphs
	* Action buttons
* Inventory
* Workflow tasks, including:
	* Input/output
	* Rollback

## License
It is licensed under the Apache 2.0 license (see LICENSE for more information).

