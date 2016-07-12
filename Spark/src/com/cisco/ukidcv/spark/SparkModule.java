package com.cisco.ukidcv.spark;

import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccountDB;
import com.cisco.ukidcv.spark.account.handler.SparkConnectionHandler;
import com.cisco.ukidcv.spark.account.handler.SparkConvergedStackBuilder;
import com.cisco.ukidcv.spark.account.handler.SparkInventoryItemHandler;
import com.cisco.ukidcv.spark.account.handler.SparkInventoryListener;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkAccountException;
import com.cloupia.fw.objstore.ObjStore;
import com.cloupia.fw.objstore.ObjStoreHelper;
import com.cloupia.lib.connector.ConfigItemDef;
import com.cloupia.lib.connector.account.AccountTypeEntry;
import com.cloupia.lib.connector.account.AccountUtil;
import com.cloupia.lib.connector.account.PhysicalAccountTypeManager;
import com.cloupia.lib.connector.account.PhysicalInfraAccount;
import com.cloupia.model.cIM.InfraAccount;
import com.cloupia.model.cIM.InfraAccountTypes;
import com.cloupia.model.cIM.ReportContextRegistry;
import com.cloupia.service.cIM.inframgr.AbstractCloupiaModule;
import com.cloupia.service.cIM.inframgr.AbstractTask;
import com.cloupia.service.cIM.inframgr.CustomFeatureRegistry;
import com.cloupia.service.cIM.inframgr.collector.controller.CollectorFactory;
import com.cloupia.service.cIM.inframgr.reports.simplified.CloupiaReport;

/**
 *
 *
 * @author Matt Day
 *
 */
public class SparkModule extends AbstractCloupiaModule {

	private static Logger logger = Logger.getLogger(SparkModule.class);

	@Override
	public CloupiaReport[] getReports() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractTask[] getTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(CustomFeatureRegistry cfr) {

		try {
			// Register LOV inputs
			// None :(

			// Register workflow inputs
			// None :(

			// Register the account type drilldown
			ReportContextRegistry.getInstance().register(SparkConstants.INFRA_ACCOUNT_TYPE,
					SparkConstants.INFRA_ACCOUNT_LABEL);

			// Create the Spark account type below
			this.createAccountType();

			//
			try {
				final ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
				final List<InfraAccount> objs = store.queryAll();
				for (final InfraAccount a : objs) {
					final PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
					// Important to check if the account type is null first
					if ((acc != null) && (acc.getAccountType() != null)
							&& (acc.getAccountType().equals(SparkConstants.INFRA_ACCOUNT_TYPE))) {
						@SuppressWarnings("unused")
						final String accountName = acc.getAccountName();
						// HP3ParInventory.init(new
						// HP3ParCredentials(accountName));
					}

				}
			}
			catch (final Exception e) {
				logger.warn("Could not start initial inventory collection");
				logger.warn("Inventory collection failed: " + e.getMessage());
				throw new SparkAccountException(e.getMessage());
			}

		}
		catch (final Exception e) {
			logger.error("Error loading Spark module.", e);
		}

	}

	// Create the plugin as an account in UCSD
	private void createAccountType() throws Exception {
		final AccountTypeEntry entry = new AccountTypeEntry();

		// Tell UCSD where and how to store account details
		entry.setCredentialClass(SparkAccountDB.class);

		// Internal account type
		entry.setAccountType(SparkConstants.INFRA_ACCOUNT_TYPE);

		// Account type label for the GUI
		entry.setAccountLabel(SparkConstants.INFRA_ACCOUNT_LABEL);

		// Type of account (can be storage, networking or compute) - this is
		// storage for now
		entry.setCategory(InfraAccountTypes.CAT_STORAGE);

		// Each account has its own context ID - set it here
		entry.setContextType(
				ReportContextRegistry.getInstance().getContextByName(SparkConstants.INFRA_ACCOUNT_TYPE).getType());

		// Make this a physical account - not sure if other types even work!
		entry.setAccountClass(AccountTypeEntry.PHYSICAL_ACCOUNT);

		// Task prefix, only used for authorised plugins
		entry.setInventoryTaskPrefix(SparkConstants.TASK_PREFIX);

		// Workflow category
		entry.setWorkflowTaskCategory(SparkConstants.WORKFLOW_CATEGORY);

		// Collection frequency - 60 minutes is the lowest you can do
		entry.setInventoryFrequencyInMins(60);

		// Pod types for this plugin - just allow Generic Pods for now
		entry.setPodTypes(new String[] {
				"GenericPod",
		});

		// This tests if the account credentials are OK or not when you try and
		// add it
		entry.setTestConnectionHandler(new SparkConnectionHandler());

		// Register an inventory listener to periodically poll
		entry.setInventoryListener(new SparkInventoryListener());

		// Add converged stack icon and detail
		entry.setConvergedStackComponentBuilder(new SparkConvergedStackBuilder());

		// You can edit this to allow Stack Designer support (not implemented
		// here!)
		// entry.setStackViewItemProvider(new FooStackViewProvider());

		// Add the plugin to the UCSD system:
		try {
			// Adding inventory root
			this.registerInventoryObjects(entry);
			PhysicalAccountTypeManager.getInstance().addNewAccountType(entry);
		}
		catch (final Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("static-method")
	private void registerInventoryObjects(AccountTypeEntry sparkRecoverPointAccountEntry) {
		@SuppressWarnings("unused")
		final ConfigItemDef HP3ParRecoverPointStateInfo = sparkRecoverPointAccountEntry
				.createInventoryRoot("Spark.inventory.root", SparkInventoryItemHandler.class);
	}

	// This method is deprecated, so return null
	@Override
	@Deprecated
	public CollectorFactory[] getCollectors() {
		return null;
	}

}
