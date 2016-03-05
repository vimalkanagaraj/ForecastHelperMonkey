package com.helpermonkey;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.olingo.ResourceOlingoMonkey;
import com.helpermonkey.olingo.RevenueOlingoMonkey;
import com.helpermonkey.report.ReportingMonkey;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.util.StaticMasterData;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ForecastMonkey {
	
	// private static final Log logger = LogFactory.getLog(NumbersMonkey.class);
	// private static final Logger logger =
	// LoggerFactory.getLogger(Thread.currentThread().getClass());
	private static final Logger logger = LoggerFactory.getLogger(ForecastMonkey.class);

	public static final String msg = "\n\nWrong Usage of ForecastMonkey. Provide the right operation to perform."
			+ "\n Supported Operations are: " + "\n	getAll (fetches all the records from Share Point)"
			+ "\n	updateAll (updates all the records from excel into the Share Point)"
			+ "\n          if id is not present then its an insert, if there is value in id column then monkey will attempt an update for that id/record.)"
			+ "\n	report (refresh gets all data from Share Point and then Creates the denormalised sheet for reporting using which you could pivot tables and creates various views)"
			+ "\n	report (does not refresh any data from SharePoint, takes the data from the excel itsels and then creates the denormalized sheet post all required calculations.)"
			+ "\n   help (provides help on how to run this monkey\n\n";

	public static final String howToUse = "\n\n How to Use ForecastMonkey - Simple Guide: "
			+ "\n Execute the below command in a command prompt or a batch file.\n\n"
			+ "     java -jar ForecastHelperMonkey.jar <operationName> [listName or optional commands]"
			+ "\n\n Eg. java -jar NumbersHelperMonkey.jar getAll Resource"
			+ "\n List name is optional, if list name is not provided then the specified operation will be performed on both Revenue & Resource lists"
			+ "\n\n Eg. java -jar NumbersHelperMonkey.jar report refresh"
			+ "\n refresh command is optionsl, but if provided the helper monkey would refresh the data form sharepoint before creating the report/denormalized sheet for pivots."
			+ "\n\nBefore executing this command ensure the following:"
			+ "\n   1. You have the necessary excel file in the respective folder."
			+ "\n   2. You have used the dropdowns in the excel to enter data OR if copy pasted not formulas like vlookup is pasted etc."
			+ "\n   3. You have updated the records as required."
			+ "\n   4. You have NOT modified the sheet names in the excel." + "\n" + "\n Supported Operations are: "
			+ "\n	getAll (fetches all the records from Share Point)"
			+ "\n	updateAll (updates all the records from excel into the Share Point)"
			+ "\n          if id is not present then its an insert, if there is value in id column then monkey will attempt an update for that id/record.)"
			+ "\n	report (refresh gets all data from Share Point and then Creates the denormalised sheet for reporting using which you could pivot tables and creates various views)"
			+ "\n	report (does not refresh any data from SharePoint, takes the data from the excel itsels and then creates the denormalized sheet post all required calculations.)"
			+ "\n   help (provides help on how to run this monkey\n\n";

	public String myCookie = null;

	public ForecastMonkey() throws Exception {
	}

	public void initialize() throws Exception {
		initialize(StaticMasterData.RnR_FORECAST_FILE);
	}

	public void initializeForReporting() throws Exception {
		initialize(StaticMasterData.RnR_REPORT_FILE);
	}

	public void initialize(String fileName) throws Exception {
		try {
			logger.info("About to read excel file:" + fileName);
			decryptPassword();
		} catch (Exception ex) {
			logger.error("Full path of excel has to be provided. exception occurred:", ex);
		}

		// take a backup of the excel file always before monkey starts jumping
		// around.
		File backupDir = new File(StaticMasterData.RnR_BACKUP_DIR);
		if (!backupDir.exists()) {
			backupDir.mkdir();
		}

		File currentFile = new File(fileName);
		double random = Math.random();
		File backupFile = new File(
				backupDir.getAbsolutePath() + "/" + (Math.round(random * 1000)) + currentFile.getName());

		// take a backup of the excel file always before monkey starts jumping
		// around.
		FileUtils.copyFile(currentFile, backupFile);

		StaticMasterData.CURRENT_FILE = fileName;

		// load the settings first
		StaticCache.loadAndCacheMasterData();

		revenueMonkey = new RevenueOlingoMonkey();
		resourceMonkey = new ResourceOlingoMonkey();
	}

	public RevenueOlingoMonkey revenueMonkey = null;
	public ResourceOlingoMonkey resourceMonkey = null;

	public static void encryptPassword(String username, String password) throws Exception {
		if (password == null) {
			throw new IllegalArgumentException(
					"value to be encrypted is null, please provide your password to be encrypted as an argumennt to the Java command.");
		}
		String authCode = StaticMasterData.DOMAIN + "||" + username + "||" + password;

		// encode with padding
		String encoded = Base64.getEncoder().encodeToString(authCode.getBytes());

		URL userPrefURL = ForecastMonkey.class.getClassLoader().getResource("UserPreferences.properties");
		String file = userPrefURL.getFile();

		Properties properties = new Properties();
		properties.load(userPrefURL.openStream());
		properties.put("encryptedPassword", encoded);
		properties.put("username", username);
		StaticMasterData.LOGGED_IN_USERNAME = username;

		FileOutputStream outputStr = new FileOutputStream(file);
		properties.store(outputStr, "changed encrypted password");

		logger.info("Encrypted Password {} is saved for all future use.:", encoded);
	}

	public static void decryptPassword() throws Exception {
		if (StaticMasterData.ENCRYPTED_PASSWORD == null) {
			throw new IllegalArgumentException("encrypted password is not present in the property file, "
					+ "this is mandatory for the program to run, please provide the encrypted password to prcoeed further");
		}
		// decode a String
		byte[] decodedPass = Base64.getDecoder().decode(StaticMasterData.ENCRYPTED_PASSWORD);

		String password = new String(decodedPass, Charset.forName("UTF-8"));
		String[] passArr = StringUtils.split(password, "||");

		if (!StaticMasterData.LOGGED_IN_USERNAME.equalsIgnoreCase(passArr[1])) {
			throw new IllegalAccessError(
					"The encrypted password is not the right password for user:" + StaticMasterData.LOGGED_IN_USERNAME);
		}

		StaticMasterData.PASSWORD = passArr[2];
	}

	public void getParentCustomers() throws Exception {
		initialize();
//		// load the parent customer data form the SP site and write it into the
//		// excel sheet as well
//		StaticCache.loadParentCustomerAndCache(true);
	}

	public void getAll(String listName, boolean initialize) throws Exception {
		if (initialize) {
			initialize();
		}
		if (listName == null) {
//			// load the parent customer data form the SP site and write it into
//			// the excel sheet as well
//			StaticCache.loadParentCustomerAndCache(true);

			revenueMonkey.getRevenueListData();
			StaticCache.loadRevenueAndCache();

			resourceMonkey.getResourceListData();
		} else if (StaticMasterData.REVENUE_LIST_NAME.equalsIgnoreCase(listName)) {
			revenueMonkey.getRevenueListData();
		} else if (StaticMasterData.RESOURCE_LIST_NAME.equalsIgnoreCase(listName)) {
			resourceMonkey.getResourceListData();
		} else {
			logger.error("Wrong list name provided. Please provide a right list name.");
		}
	}

	public void update(String listName) throws Exception {
		initialize();
		if (listName == null) {
			revenueMonkey.updateRevenueRecord();
			resourceMonkey.updateResourceRecord();
		} else if (StaticMasterData.REVENUE_LIST_NAME.equalsIgnoreCase(listName)) {
			revenueMonkey.updateRevenueRecord();
		} else if (StaticMasterData.RESOURCE_LIST_NAME.equalsIgnoreCase(listName)) {
			resourceMonkey.updateResourceRecord();
		} else {
			logger.error("Wrong list name provided. Please provide a right list name.");
		}
	}

	public void createReport(String refreshData) throws Exception {
		initializeForReporting();
		if ("refreshData".equalsIgnoreCase(refreshData)) {
			// getParentCustomers();
			getAll(null, false);
		}

		ReportingMonkey rptMonkey = new ReportingMonkey();
		rptMonkey.createMonkeyReport();
	}

	public static void main(String[] args) throws IOException {
		try {
			logger.info("Waking up NumbersMonkey to roll....");
			ForecastMonkey monkey = new ForecastMonkey();

			long startTime = System.currentTimeMillis();
			logger.info("Start Time is {} ", startTime);

			switch (args[0]) {
			case "getAll":
				monkey.getAll((args.length > 1) ? args[1] : null, true);
				break;
			case "updateAll":
				monkey.update((args.length > 1) ? args[1] : null);
				break;
			case "encryptPassword":
				ForecastMonkey.encryptPassword(args[1], args[2]);
				break;
			case "getParentCustomers":
				monkey.getParentCustomers();
				break;
			case "report":
				monkey.createReport((args.length > 1) ? args[1] : null);
				break;
			case "help":
				logger.info(howToUse);
				break;
			default:
				logger.error(msg);
			}

			long endTime = System.currentTimeMillis();
			logger.info("End Time is {}", endTime);
			logger.info("Total Time for processing is {} seconds):", (endTime - startTime) / 1000);

		} catch (Exception ex) {
			logger.error("Error caught in main: ", ex);
		}
	}
}

// String str = "$filter=Oppty_ProjectName eq \'PI APS Solution\'";
// str = Base64.getEncoder().encode(str.getBytes()).toString();
// queryString = URLEncoder.encode(queryString, "UTF-8");

/* *** Revenue List Queries *** */
// restMonkey.getRevenueTopFewRows(5);
// String filterString = "((Id eq 1085) or (Id eq 809))";
// restMonkey.filterRevenueList(filterString);
// restMonkey.getRevenueListMetadata();

/* *** Resource List Queries *** */
// restMonkey.fetchEntireResourceList();
// restMonkey.getResourceTopFewRows(5);
// restMonkey.getTopFewRows(5, BDMConstants.RESOURCE_LIST_NAME);
// String queryString = "Revenue/OpptyProjectName eq 'PI APS
// Solution'";
