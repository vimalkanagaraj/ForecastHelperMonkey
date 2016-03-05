package com.helpermonkey.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.excel.ExcelUtil;
import com.helpermonkey.excel.ProjectExcelHandler;
import com.helpermonkey.excel.SettingsExcelHandler;
import com.helpermonkey.olingo.MasterDataOlingoMonkey;
import com.helpermonkey.olingo.ParentCustomerOlingoMonkey;
import com.helpermonkey.vo.MonkeyRolesVO;
import com.helpermonkey.vo.ParentCustomerVO;
import com.helpermonkey.vo.ProjectVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class StaticCache {

	private static final String ALL = "ALL";

	private static final String INDIRECT_COST = "Indirect Cost";

	private static final String DIRECT_COST = "Direct Cost";

	private static final Logger logger = LoggerFactory.getLogger(StaticCache.class);

	public static HashMap<Integer, Integer> weekDayMap = new HashMap<Integer, Integer>();

	public static HashMap<String, ArrayList<Integer>> locationHolidays = new HashMap<String, ArrayList<Integer>>();

	public static HashMap<Double, ArrayList<Integer>> vacations = new HashMap<Double, ArrayList<Integer>>();

	public static ArrayList<ParentCustomerVO> parentCustomers = new ArrayList<ParentCustomerVO>();

	public static ArrayList<ProjectVO> revenueList = new ArrayList<ProjectVO>();

	public static ArrayList<MonkeyRolesVO> rolesList = new ArrayList<MonkeyRolesVO>();

	public static HashMap<String, ArrayList<String>> masterDataMap = new HashMap<String, ArrayList<String>>();

	public static HashMap<String, Double> standardCostMap = new HashMap<String, Double>();

	public static MonkeyRolesVO loggedInUser = null;

	public static int getBillableHours(String customerName) {
		ParentCustomerVO parentCustomer = parentCustomers
				.get(parentCustomers.indexOf(new ParentCustomerVO().setParentCustomerName(customerName)));
		if (parentCustomer == null) {
			return 0;
		}
		return parentCustomer.getBillableHours();
	}

	public static double getDiscount(String customerName) {
		ParentCustomerVO parentCustomer = parentCustomers
				.get(parentCustomers.indexOf(new ParentCustomerVO().setParentCustomerName(customerName)));
		if (parentCustomer == null) {
			return 0;
		}
		return parentCustomer.getDiscount();
	}

	public static double getMonthlyDirectCost(String grade, String location) throws Exception {
		String locationCountry = getLocationCountry(location);
		String key = DIRECT_COST + locationCountry + location + grade;

		Double monthlyCost = standardCostMap.get(key);
		if (monthlyCost == null) {
			key = DIRECT_COST + locationCountry + ALL + grade;
			monthlyCost = standardCostMap.get(key);
			if (monthlyCost == null) {
				return 0;
			}
		}
		return monthlyCost;
	}

	public static double getMonthlyIndirectCost(String grade, String location) throws Exception {
		String locationCountry = getLocationCountry(location);
		String key = INDIRECT_COST + locationCountry + location + grade;

		Double monthlyCost = standardCostMap.get(key);
		if (monthlyCost == null) {
			key = INDIRECT_COST + locationCountry + location + ALL;
			monthlyCost = standardCostMap.get(key);
			if (monthlyCost == null) {
				return 0;
			}
		}
		return monthlyCost;
	}

	private static ParentCustomerVO getPCustVO(int index) {
		if (index < 0) {
			return null;
		}

		return parentCustomers.get(index);
	}

	public static ParentCustomerVO getParentCustomerByName(String customerName) {
		int index = parentCustomers.indexOf(new ParentCustomerVO().setParentCustomerName(customerName));
		return getPCustVO(index);
	}

	public static ParentCustomerVO getParentCustomerById(int customerId) {
		int index = parentCustomers.indexOf(new ParentCustomerVO().setId(customerId));
		return getPCustVO(index);
	}

	public static String getParentCustomerName(int parentCustomerId) {
		ParentCustomerVO parentCustomer = getParentCustomerById(parentCustomerId);
		if (parentCustomer == null) {
			return null;
		}
		return parentCustomer.getParentCustomerName();
	}

	public static int getParentCustomerId(String parentCustomerName) {
		ParentCustomerVO parentCustomer = getParentCustomerByName(parentCustomerName);
		if (parentCustomer == null) {
			return 0;
		}
		return parentCustomer.getId();
	}

	public static ArrayList<Integer> getDEMForLoggedInUserVertical() {
		ArrayList<Integer> demList = new ArrayList<Integer>();
		if (MonkeyConstants.ROLE_DBP.equalsIgnoreCase(loggedInUser.getRole())
				|| MonkeyConstants.ROLE_OPS.equalsIgnoreCase(loggedInUser.getRole())) {
			for (Iterator<MonkeyRolesVO> iterator = rolesList.iterator(); iterator.hasNext();) {
				MonkeyRolesVO roleVO = iterator.next();
				if (loggedInUser.getVertical().equalsIgnoreCase(roleVO.getVertical())
						&& loggedInUser.getLocation().equalsIgnoreCase(roleVO.getLocation())) {
					demList.add(roleVO.getId());
				}
			}
		}
		return demList;
	}

	public static boolean canDEMModifyRecord(String parentCustomerName) {
		ParentCustomerVO pcustVO;
		if ((pcustVO = getParentCustomerByName(parentCustomerName)) == null) {
			logger.debug("Customer name {} not existing, hence returning false in canDEMModifyRecord.",
					parentCustomerName);
			return false;
		}

		return canDEMModifyRecord(pcustVO);
	}

	public static boolean canDEMModifyRecord(ParentCustomerVO pcustVO) {

		// DBP can modify any data for any customer within his/her vertical,
		// same holds good for OPS
		if (loggedInUser.isRoleDBP() || loggedInUser.isRoleOPS()) {
			if (loggedInUser.getVertical().equalsIgnoreCase(pcustVO.getVertical())) {
				return true;
			} else {
				return false;
			}
		}

		if (loggedInUser.isOnsite()) {
			if (pcustVO.getDEMOnsiteId() == loggedInUser.getId()) {
				return true;
			} else {
				return false;
			}
		} else {
			if (pcustVO.getDEMOffshoreId() == loggedInUser.getId()) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static ProjectVO getRevenueItem(String opptyProjectName) {
		ProjectVO revVO = new ProjectVO();
		revVO.setOpptyProjectName(opptyProjectName);
		int index = StaticCache.revenueList.indexOf(revVO);
		if (index > -1) {
			return StaticCache.revenueList.get(index);
		}
		return null;
	}

	public static ArrayList<ProjectVO> cloneRevenueList() {
		ArrayList<ProjectVO> revList = new ArrayList<ProjectVO>();
		revList.addAll(StaticCache.revenueList);
		return revList;
	}

	public static void loadParentCustomerAndCache() throws Exception {
		getLoggedInDEM();

		if (parentCustomers == null || parentCustomers.size() < 1) {
			ParentCustomerOlingoMonkey pcustMonkey = new ParentCustomerOlingoMonkey();
			parentCustomers = pcustMonkey.getParentCustomerList();
		}
	}

	public static void loadRevenueAndCache() throws Exception {
		ProjectExcelHandler excelHandler = new ProjectExcelHandler();

		// read the revcenue list from the Excel file
		revenueList = excelHandler.readRevenueSheetFromExcel();
	}

	public static MonkeyRolesVO getLoggedInDEM() throws Exception {
		if (loggedInUser == null) {
			loadMonkeyRoles();

			int loggedInUserAssociateId = new Integer(StaticMasterData.LOGGED_IN_USERNAME).intValue();
			for (int i = 0; i < rolesList.size(); i++) {
				MonkeyRolesVO roleVO = rolesList.get(i);
				if (loggedInUserAssociateId == roleVO.getAssociateId()) {
					loggedInUser = roleVO;
				}
			}

			if (loggedInUser == null) {
				throw new IllegalAccessError("logged in user " + StaticMasterData.LOGGED_IN_USERNAME
						+ " not properly setup in MonkeyRoles. please verify MonkeyRoles.");
			}
		}
		return loggedInUser;
	}

	public static String getDEMAssociateName(int demId) throws Exception {
		if (rolesList == null || rolesList.size() < 1) {
			MasterDataOlingoMonkey mdMonkey = new MasterDataOlingoMonkey();
			rolesList = mdMonkey.getMonkeyRoles();
		}

		int index = rolesList.indexOf(new MonkeyRolesVO().setId(demId));
		if (index < 0) {
			return null;
		}

		MonkeyRolesVO roleVO = rolesList.get(index);

		return roleVO.getAssociateName();
	}

	public static int getOnsiteDEMId(String customerName) throws Exception {
		// if (getLoggedInDEM().isOnsite() && (!getLoggedInDEM().isRoleDBP())) {
		// return StaticCache.getLoggedInDEM().getId();
		// } else {
		// return
		// StaticCache.getParentCustomerByName(customerName).getDEMOnsiteId();
		// }
		return StaticCache.getParentCustomerByName(customerName).getDEMOnsiteId();
	}

	public static int getOffshoreDEMId(String customerName) throws Exception {
		// if (getLoggedInDEM().isOnsite() || getLoggedInDEM().isRoleDBP()) {
		// return
		// StaticCache.getParentCustomerByName(customerName).getDEMOffshoreId();
		// } else {
		// return StaticCache.getLoggedInDEM().getId();
		// }
		return StaticCache.getParentCustomerByName(customerName).getDEMOffshoreId();
	}

	// public static ArrayList<String> getProjectTypes() throws Exception {
	// loadMasterDataMap();
	// ArrayList<String> projectTypes =
	// masterDataMap.get(StaticMasterData.PROJECT_TYPE_KEY);
	// if (projectTypes == null) {
	// // vimal sending empty list
	// return new ArrayList<String>();
	// }
	// return projectTypes;
	// }

	public static boolean isValidValue(String value, String masterDataKey) throws Exception {
		loadMasterDataMap();
		ArrayList<String> valueList = masterDataMap.get(masterDataKey);
		if (valueList == null) {
			return false;
		}

		for (String mdValue : valueList) {
			if (mdValue.equalsIgnoreCase(value)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isValidBillability(String billabilityValue) throws Exception {
		return isValidValue(billabilityValue, StaticMasterData.BILLABILITY_KEY);
	}

	public static boolean isValidCompetency(String competencyName) throws Exception {
		return isValidValue(competencyName, StaticMasterData.COMPETENCY_KEY);
	}

	public static boolean isValidSkillset(String competencyName, String skillsetValue) throws Exception {
		return isValidValue(skillsetValue, competencyName);
	}

	public static boolean isValidGrade(String gradeName) throws Exception {
		return isValidValue(gradeName, StaticMasterData.GRADE_KEY);
	}

	public static boolean isValidLocation(String locationName) throws Exception {
		boolean validLocation = false;
		ArrayList<String> countryList = getLocationCountries();
		for (String countryName : countryList) {
			validLocation = isValidValue(locationName, countryName);
			if (validLocation) {
				return validLocation;
			}
		}
		return validLocation;
	}

	public static boolean isValidParentCustomer(String parentCustomerName) throws Exception {
		if (StaticCache.getParentCustomerByName(parentCustomerName) == null) {
			return false;
		}

		return true;
	}

	public static boolean isValidProjectStatus(String projectStatus) throws Exception {
		return isValidValue(projectStatus, StaticMasterData.PROJECT_STATUS_KEY);
	}

	public static boolean isValidProjectType(String projectType) throws Exception {
		return isValidValue(projectType, StaticMasterData.PROJECT_TYPE_KEY);
	}

	public static boolean isValidRegion(String region) throws Exception {
		return isValidValue(region, StaticMasterData.REGION_LIST_KEY);
	}

	public static boolean isValidResourceStatus(String region) throws Exception {
		return isValidValue(region, StaticMasterData.RESOURCE_STATUS_KEY);
	}

	public static ArrayList<String> getSkills(String competencyName) throws Exception {
		loadMasterDataMap();
		ArrayList<String> skillsList = masterDataMap.get(competencyName);
		if (skillsList == null) {
			return new ArrayList<String>();
		}
		return skillsList;
	}

	public static ArrayList<String> getCompetencies() throws Exception {
		loadMasterDataMap();
		ArrayList<String> competencyList = masterDataMap.get(StaticMasterData.COMPETENCY_KEY);
		if (competencyList == null) {
			return new ArrayList<String>();
		}
		return competencyList;
	}

	public static ArrayList<String> getLocationCountries() throws Exception {
		loadMasterDataMap();
		ArrayList<String> countryList = masterDataMap.get(StaticMasterData.LOCATION_COUNTRY_KEY);
		if (countryList == null) {
			return new ArrayList<String>();
		}

		return countryList;
	}

	public static String getLocationCountry(String locationName) throws Exception {
		ArrayList<String> countryList = getLocationCountries();
		for (String countryName : countryList) {
			ArrayList<String> valueList = masterDataMap.get(countryName);

			for (String mdLocation : valueList) {
				if (mdLocation.equalsIgnoreCase(locationName)) {
					return countryName;
				}
			}
		}

		return "";
	}

	public static Set<String> getMasterDataListNames() throws Exception {
		loadMasterDataMap();
		return masterDataMap.keySet();
	}

	public static void loadMasterDataMap() throws Exception {
		if (masterDataMap == null || masterDataMap.size() < 1) {
			MasterDataOlingoMonkey mdMonkey = new MasterDataOlingoMonkey();
			// set the map to static memory so it is accessible throughout the
			// application
			StaticCache.masterDataMap = mdMonkey.getMasterDataMap();
		}
	}

	public static void loadStandardCostMap() throws Exception {
		MasterDataOlingoMonkey mdMonkey = new MasterDataOlingoMonkey();

		if (standardCostMap == null || standardCostMap.size() < 1) {
			// set the map to static memory so it is accessible throughout the
			// application
			StaticCache.standardCostMap = mdMonkey.getStandardCostMap();
		}
	}

	public static void loadMonkeyRoles() throws Exception {
		if (rolesList == null || rolesList.size() < 1) {
			MasterDataOlingoMonkey mdMonkey = new MasterDataOlingoMonkey();
			rolesList = mdMonkey.getMonkeyRoles();
		}
	}

	public static void loadAndCacheMasterData() throws Exception {
		SettingsExcelHandler handler = new SettingsExcelHandler();
		Workbook workbook = ExcelUtil.createWorkbook(true);

		// set the list to static memory so it is accessible throughout the
		// application
		StaticCache.locationHolidays = handler.loadHolidaysFromExcel(workbook);

		// set the list to static memory so it is accessible throughout the
		// application
		StaticCache.vacations = handler.loadVacationsFromExcel(workbook);

		loadMasterDataMap();

		loadStandardCostMap();
		
		loadMonkeyRoles();
		
		getLoggedInDEM();

		loadParentCustomerAndCache();
		handler.writeParentCustomerToExcel(workbook, parentCustomers, true);

		ExcelUtil.writeAndClose(workbook);

		// At the start load the revenue data and cache it such that it can be
		// used for updates and also any
		// downstream object creates like resource creation etc.
		loadRevenueAndCache();

	}
}
