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
Please see the **[installation guide](INSTALL.md)** for instructions on how to compile and install this plugin.

## Demo Video
[![Video Demo](http://img.youtube.com/vi/N4l2K2TbfKE/0.jpg)](https://www.youtube.com/watch?v=N4l2K2TbfKE)

You can find [screenshots here](screenshots.md)

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

