/*******************************************************************************
 * Copyright (c) 2016 Matt Day, Cisco and others
 *
 * Unless explicitly stated otherwise all files in this repository are licensed
 * under the Apache Software License 2.0
 *******************************************************************************/
package com.cisco.ukidcv.spark.account.inventory;

/**
 * Runs every 60 minutes or after a user action (e.g. adding a city) and
 * collects information to store locally
 * <p>
 * This is handled in two parts - this inventory class providing static methods
 * and a database store
 *
 * @author Matt Day
 * @see com.cisco.ukidcv.spark.account.inventory.SparkInventoryDB
 *
 */
public class SparkInventory {

}
