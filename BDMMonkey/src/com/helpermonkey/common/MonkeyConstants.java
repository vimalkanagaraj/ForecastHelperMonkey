package com.helpermonkey.common;

import com.helpermonkey.util.StaticMasterData;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MonkeyConstants {

	public static final String DATE_FORMAT = Messages.getString("dateFormat"); 
	public static final String INT_FORMAT = Messages.getString("intformat"); 
	public static final String DECIMAL_FORMAT = Messages.getString("decimalFormat"); 

	public static final String HOST = Messages.getString("host"); 
	public static final int PORT = 443;
	public static final String BDM_SITE_URL = Messages.getString("siteUrl"); 

	// https://ch1hub.cognizant.com/CookieAuth.dll?Logon
	// public static final String BDM_CONTENTINFO_URL =
	// "https://ch1hub.cognizant.com/sites/SC2268/DEPBFSIBDM/_api/web/contextinfo";
	public static final String BDM_CONTENTINFO_URL = BDM_SITE_URL + Messages.getString("contextUrl"); 
	public static final String LIST_URL = BDM_SITE_URL + Messages.getString("listDataUrl"); 
	public static final String LIST_METADATA_URL = LIST_URL + Messages.getString("metadataUrl"); 

	public static final String REVENUE_LIST_URL = LIST_URL + StaticMasterData.REVENUE_LIST_NAME;
	public static final String REVENUE_ITEM_TOP3 = REVENUE_LIST_URL + Messages.getString("topQueryStr"); 

	public static final String RESOURCE_LIST_URL = LIST_URL + StaticMasterData.RESOURCE_LIST_NAME;
	public static final String RESOURCE_ITEM_TOP3 = RESOURCE_LIST_URL + "?$top=3"; 
	public static final String RESOURCE_ITEM_17 = RESOURCE_LIST_URL + Messages.getString("qryStr17"); 

	public static final String CALCULATED_LIST_URL = LIST_URL + StaticMasterData.CALCULATED_LIST_NAME;
	public static final String PARENTCUSTOMER_LIST_URL = LIST_URL + StaticMasterData.PARENTCUSTOMER_LIST_NAME;
	public static final String RATECARD_LIST_URL = LIST_URL + StaticMasterData.RATECARD_LIST_NAME;
	public static final String MONKEY_ROLES_LIST_URL = LIST_URL + StaticMasterData.MONKEY_ROLES_LIST_NAME;

	public static final String MASTERDATA_LIST_URL = LIST_URL + StaticMasterData.MASTERDATA_LIST_NAME;
	public static final String STD_COST_LIST_URL = LIST_URL + StaticMasterData.STD_COST_LIST_NAME;
	public static final String LEAVE_REGISTER_LIST_URL = LIST_URL + StaticMasterData.LEAVE_REGISTER_LIST_NAME;
	
	public static final double DEFAULT_CONFIDENCE = 0.1;
	public static final String GET_METHOD = "GET"; 
	public static final String POST_METHOD = "POST"; 
	public static final String SET_COOKIE_HEADER = Messages.getString("cookieHttpHeader"); 
	
	public static final String CHANGE_FLAG_SUCCESS = Messages.getString("changeFlag_S"); 
	public static final String CHANGE_FLAG_UPDATE = Messages.getString("changeFlag_Y"); 
	public static final String CHANGE_FLAG_ERROR = Messages.getString("changeFlag_E");
	
	public static final String OFFSHORE_LOCATION_VALUE = Messages.getString("offshoreLocationValue"); ;
	public static final String USA_LOCATION_VALUE = Messages.getString("usaLocationValue"); 
	public static final String UK_LOCATION_VALUE = Messages.getString("ukLocationValue"); 

	public static final String LOCATION_UK = Messages.getString("ukKey"); 
	public static final String LOCATION_USA = Messages.getString("usaKey"); 
	
	public static final String RATE_CARD_ENTRY = Messages.getString("rateCardEntry"); 

	public static final String DATE_SEPERATOR = Messages.getString("dateSeperator"); 

	public static final int CURRENT_YR = new Integer(Messages.getString("currentYear")).intValue(); 
	public static final int PREVIOUS_YR = new Integer(Messages.getString("previousYear")).intValue(); 
	
	public static final String ROLE_DBP = "DBP";
	public static final String ROLE_DEM = "DEM";
	public static final String ROLE_OPS = "OPS";
	
	/* *** properties from UserPreferences.properties file *** */
	
	public static final String NUMBER_STYLE = UserPreferences.getString("numberExcelStyle"); 
	public static final String CURRENCY_STYLE = UserPreferences.getString("currencyExcelStyle"); 
	public static final String DATE_STYLE = UserPreferences.getString("dateExcelStyle"); 
	public static final String PERCENT_STYLE = UserPreferences.getString("percentExcelStyle");
	public static final String FLD_DEM_ONSITE = "DEMOnsiteId";
	public static final String FLD_DEM_OFFSHORE = "DEMOffshoreId";
	public static final String FULFILLED = "Fulfilled";
	public static final double MAX_ALLOCATION_FTE = 1.0;
	public static final String BTM_PROJECT_TYPE = "BTM";

}
