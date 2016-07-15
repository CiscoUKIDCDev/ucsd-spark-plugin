package com.cisco.ukidcv.spark;

import java.util.List;

import org.apache.log4j.Logger;

import com.cisco.ukidcv.spark.account.SparkAccount;
import com.cisco.ukidcv.spark.account.SparkAccountDB;
import com.cisco.ukidcv.spark.account.SparkConvergedStackBuilder;
import com.cisco.ukidcv.spark.account.handler.SparkConnectionHandler;
import com.cisco.ukidcv.spark.account.handler.SparkInventoryItemHandler;
import com.cisco.ukidcv.spark.account.handler.SparkInventoryListener;
import com.cisco.ukidcv.spark.account.inventory.SparkInventory;
import com.cisco.ukidcv.spark.constants.SparkConstants;
import com.cisco.ukidcv.spark.exceptions.SparkAccountException;
import com.cisco.ukidcv.spark.inputs.SparkAccountSelector;
import com.cisco.ukidcv.spark.inputs.SparkMessageSelector;
import com.cisco.ukidcv.spark.inputs.SparkRoomSelector;
import com.cisco.ukidcv.spark.inputs.WorkflowInputTypeDeclaration;
import com.cisco.ukidcv.spark.reports.inventory.InventoryReport;
import com.cisco.ukidcv.spark.reports.rooms.SparkRoomReport;
import com.cisco.ukidcv.spark.reports.summary.AccountReport;
import com.cisco.ukidcv.spark.tasks.inventory.CollectInventoryTask;
import com.cisco.ukidcv.spark.tasks.membership.AddMembershipTask;
import com.cisco.ukidcv.spark.tasks.membership.DeleteMembershipTask;
import com.cisco.ukidcv.spark.tasks.membership.EditMembershipTask;
import com.cisco.ukidcv.spark.tasks.messages.DeleteMessageTask;
import com.cisco.ukidcv.spark.tasks.messages.PostMessageTask;
import com.cisco.ukidcv.spark.tasks.rooms.CreateRoomTask;
import com.cisco.ukidcv.spark.tasks.rooms.DeleteRoomTask;
import com.cisco.ukidcv.spark.tasks.rooms.EditRoomTask;
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
 * This is the main entry point for the plugin.
 * <p>
 * Everything it can do is initialised here including tasks, reports, list of
 * values and so on.
 *
 * @author Matt Day
 *
 */
public class SparkModule extends AbstractCloupiaModule {

	private static Logger logger = Logger.getLogger(SparkModule.class);

	/**
	 * Provide a list of top-level reports to show in the summary/converged
	 * view. The order here is the order they'll show up in the GUI
	 */
	@Override
	public CloupiaReport[] getReports() {
		return new CloupiaReport[] {
				new AccountReport(), new SparkRoomReport(), new InventoryReport(),
		};
	}

	/**
	 * Provide a list of tasks to add to the orchestration engine
	 */
	@Override
	public AbstractTask[] getTasks() {
		return new AbstractTask[] {
				new CreateRoomTask(), new EditRoomTask(), new DeleteRoomTask(), new CollectInventoryTask(),
				new AddMembershipTask(), new EditMembershipTask(), new DeleteMembershipTask(), new PostMessageTask(),
				new DeleteMessageTask(),
		};
	}

	@Override
	public void onStart(CustomFeatureRegistry cfr) {

		try {
			// Register LOV inputs
			cfr.registerTabularField(SparkConstants.ACCOUNT_LIST_FORM_PROVIDER, SparkAccountSelector.class, "0", "0");
			cfr.registerTabularField(SparkConstants.ROOM_LIST_FORM_PROVIDER, SparkRoomSelector.class, "0", "2");
			cfr.registerTabularField(SparkConstants.MESSAGE_LIST_FORM_PROVIDER, SparkMessageSelector.class, "0", "4");

			// Register the account type drilldown
			ReportContextRegistry.getInstance().register(SparkConstants.INFRA_ACCOUNT_TYPE,
					SparkConstants.INFRA_ACCOUNT_LABEL);

			// You have to register each kind of report you want to use here or
			// they won't be able to drilldown
			ReportContextRegistry.getInstance().register(SparkConstants.ROOM_LIST_DRILLDOWN,
					SparkConstants.ROOM_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(SparkConstants.TEAM_LIST_DRILLDOWN,
					SparkConstants.TEAM_LIST_DRILLDOWN_LABEL);
			ReportContextRegistry.getInstance().register(SparkConstants.INVENTORY_LIST_DRILLDOWN,
					SparkConstants.INVENTORY_LIST_DRILLDOWN_LABEL);

			// Register workflow inputs - this is done in a separate file
			WorkflowInputTypeDeclaration.registerWFInputs();

			// Create the Spark account type below
			this.createAccountType();

			/*
			 * Initialise account inventory. This is a bit convoluted.
			 *
			 * Step one is to loop through all accounts in UCS Director and find
			 * any that are Spark accounts.
			 *
			 * Step two is to initialise the SparkInventory which will do an
			 * initial pull from the API server and cache it for reports
			 */
			try {
				final ObjStore<InfraAccount> store = ObjStoreHelper.getStore(InfraAccount.class);
				final List<InfraAccount> objs = store.queryAll();
				for (final InfraAccount a : objs) {
					final PhysicalInfraAccount acc = AccountUtil.getAccountByName(a.getAccountName());
					// Important to check if the account type is null first
					if ((acc != null) && (acc.getAccountType() != null)
							&& (acc.getAccountType().equals(SparkConstants.INFRA_ACCOUNT_TYPE))) {

						// Collect inventory
						final SparkAccount account = new SparkAccount(acc.getAccountName());
						SparkInventory.update(account, SparkConstants.INVENTORY_REASON_INITIAL, true);
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
