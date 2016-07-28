# Spark Plugin Installation Guide
To compile this plugin, you will need:

* [Java JDK](https://www.oracle.com/technetwork/java/javase/downloads) 1.8 or later
* [Ant](https://ant.apache.org/)

## Steps to compile
1. Clone or download the plugin source code. 
2. Open a terminal session and open the ```Spark``` folder
3. Execute the command ```ant build```

You will have a new file called ```spark-plugin.zip```.

## Installing in UCS Director
1. Under **Administration -> Open Automation** upload the plugin file (**Spark-plugin.zip**). Select it and click **enable**.
2. Once it has finished uploading, select the plugin and click **Enable** to mark it as active
3. ssh to your UCS Director installation as shelladmin and select **3** to stop services and y to confirm
4. Select **4** to start services again

UCS Director will reload. This may take up to 10 minutes.

Once it has come back online, navigate to **Administration -> Physical Accounts**, select the **Physical Accounts** tab and click **Add**.

Select **Storage** as the account type (yes, I know...) and then select **Spark**. You can only add it to a Generic Pod.
