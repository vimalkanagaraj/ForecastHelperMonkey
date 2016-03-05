package com.helpermonkey.util;

import java.io.File;

import com.helpermonkey.common.Messages;
import com.helpermonkey.common.UserPreferences;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class StaticMasterData {

	public static String REVENUE_LIST_NAME = Messages.getString("revenueList");
	public static String RESOURCE_LIST_NAME = Messages.getString("resourceList");
	public static String CALCULATED_LIST_NAME = Messages.getString("calculatedList");
	public static String PARENTCUSTOMER_LIST_NAME = Messages.getString("parentCustomerList");
	public static String RATECARD_LIST_NAME = Messages.getString("rateCardList");
	public static String MONKEY_ROLES_LIST_NAME = Messages.getString("monkeyRolesList");
	
	public static String MASTERDATA_LIST_NAME = Messages.getString("masterDataList");
	public static String STD_COST_LIST_NAME = Messages.getString("standardCostList");
	public static String LEAVE_REGISTER_LIST_NAME = Messages.getString("leaveRegisterList");
	
	public static String BILLABILITY_KEY = "Billability";
	public static String COMPETENCY_KEY = "Competency";
	public static String GRADE_KEY = "Grade";
	public static String LOCATION_COUNTRY_KEY = "Location Country";
	public static String PROJECT_STATUS_KEY = "Project Status";
	public static String PROJECT_TYPE_KEY = "Project Type";
	public static String REGION_LIST_KEY = "Region List";
	public static String RESOURCE_STATUS_KEY = "Resource Status";

	
	public static String DOMAIN = UserPreferences.getString("domain");
	public static String LOGGED_IN_USERNAME = UserPreferences.getString("username");
	public static String ENCRYPTED_PASSWORD = UserPreferences.getString("encryptedPassword");
	public static String PASSWORD = null;
	
	static File currentDir = new File("");
	
	public static final String RnR_BACKUP_DIR = currentDir.getAbsolutePath() + UserPreferences.getString("backupDir");

	public static String RnR_FORECAST_FILE_NAME = UserPreferences.getString("RnRForecastFilePath");
	public static String RnR_REPORT_FILE_NAME = UserPreferences.getString("ReportFilePath");

	public static String RnR_FORECAST_FILE = currentDir.getAbsolutePath() + UserPreferences.getString("RnRForecastFilePath");
	public static String RnR_REPORT_FILE = currentDir.getAbsolutePath() + UserPreferences.getString("ReportFilePath");
	
	public static String CURRENT_FILE = null;
	public static boolean REPORTING = false;

	public static String MASTER_DATA_SHEET_NAME = UserPreferences.getString("masterDataSheetName");
	public static String HOLIDAYS_SHEET_NAME = UserPreferences.getString("holidaysSheetName");
	public static String REVENUE_SHEET_NAME = UserPreferences.getString("revenueSheetName");
	public static String RESOURCE_SHEET_NAME = UserPreferences.getString("resourceSheetName");
	public static String CALCULATED_DATA_SHEET_NAME = UserPreferences.getString("calculatedSheetName");
	public static String VACATIONS_SHEET_NAME = UserPreferences.getString("vactionsSheetName");
	public static String RATE_CARD_SHEET_NAME = UserPreferences.getString("rateCardSheetName");
	public static String PARENT_CUSTOMER_SHEET_NAME = UserPreferences.getString("parentCustomerSheetName");
	public static String COST_SHEET_NAME = UserPreferences.getString("costSheetName");

}
