package com.helpermonkey.transformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.api.domain.ClientObjectFactory;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.core.ODataClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.vo.MonkeyRolesVO;
import com.helpermonkey.vo.ParentCustomerVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MasterDataTransformer extends AbstractTransformer {

	private static final Logger logger = LoggerFactory.getLogger(MasterDataTransformer.class);

	public static final String FQNAME_REVENUE_LIST = "Microsoft.SharePoint.DataService.ProjectsItem";

	public static final String FLD_DEM_ONSITE_ID = "DEMOnsiteId";

	public static final String FLD_DEM_OFFSHORE_ID = "DEMOffshoreId";

	public static final String FLD_DISCOUNT = "Discount";

	public static final String FLD_BILLABLE_HOURS = "BillableHours";

	public static final String FLD_PARENT_CUSTOMER_NAME = "ParentCustomerName";

	/* *** Rate Card list related fields *** */

	public static final String FLD_PROJECT_ROLE = "ProjectRoleValue";

	public static final String FLD_RATE_CARD = "RateCard";

	public static final String FLD_PARENT_CUSTOMER_ID = "ParentCustomerId";

	/* *** MasterData list related fields *** */

	public static final String FLD_MD_LISTNAME = "ListName";

	public static final String FLD_MD_LISTVALUE = "ListValue";

	/* *** Standard Cost list related fields *** */

	public static final String FLD_SD_COST_TYPE = "CostType";

	public static final String FLD_SD_LOCATION_COUNTRY = "LocationCountry";

	public static final String FLD_SD_LOCATION = "Location";

	public static final String FLD_SD_GRADE = "Grade";

	public static final String FLD_SD_MONTHLY_COST = "MonthlyCost";
	
	/* *** Monkey Roles list related fields *** */

	public static final String FLD_ASSOCIATE_ID = "AssociateId";

	public static final String FLD_ASSOCIATE_NAME = "AssociateName";

	public static final String FLD_ROLE = "RoleValue";

	public static final String FLD_VERTICAL = "VerticalValue";

	public static final String FLD_LOCATION = "LocationValue";

	ClientObjectFactory objFactory = null;

	RevenueTransformer transformer = null;

	public MasterDataTransformer() throws Exception {
		// objFactory = new RevenueOlingoMonkey().client.getObjectFactory();
		objFactory = ODataClientFactory.getClient().getObjectFactory();
	}

	public ParentCustomerVO toParentCustomerVO(ClientEntity entity) throws Exception {
		ParentCustomerVO pcustVO = new ParentCustomerVO();
		List<ClientProperty> properties = entity.getProperties();

		for (ClientProperty property : properties) {
			if (FLD_ID.equalsIgnoreCase(property.getName())) {
				pcustVO.setId(getIntValue(property, 0));
			}
			if (FLD_PARENT_CUSTOMER_NAME.equalsIgnoreCase(property.getName())) {
				pcustVO.setParentCustomerName(property.getValue().toString());
			}
			if (FLD_BILLABLE_HOURS.equalsIgnoreCase(property.getName())) {
				pcustVO.setBillableHours(getIntValue(property, 0));
			}
			if (FLD_DISCOUNT.equalsIgnoreCase(property.getName())) {
				pcustVO.setDiscount(getDoubleValue(property, 0));
			}
			if (FLD_VERTICAL.equalsIgnoreCase(property.getName())) {
				pcustVO.setVertical(property.getValue().toString());
			}
			if (FLD_DEM_OFFSHORE_ID.equalsIgnoreCase(property.getName())) {
				pcustVO.setDEMOffshoreId(getIntValue(property, 0));
			}
			if (FLD_DEM_ONSITE_ID.equalsIgnoreCase(property.getName())) {
				pcustVO.setDEMOnsiteId(getIntValue(property, 0));
			}
		}

		return pcustVO;
	}

	public ArrayList<ParentCustomerVO> toParentCustomerVO(
			ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator) throws Exception {

		ArrayList<ParentCustomerVO> pcustList = new ArrayList<ParentCustomerVO>();

		int recordCount = 0;
		while (iterator.hasNext()) {
			ClientEntity parentCustomer = iterator.next();

			ParentCustomerVO pcustVO = toParentCustomerVO(parentCustomer);
			pcustList.add(pcustVO);

			logger.debug("Record Number {}. Transformed Parent Customer ID {}.", ++recordCount, pcustVO.getId());
		}
		return pcustList;
	}

	public HashMap<String, ArrayList<String>> toMasterDataMap(
			ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator) throws Exception {

		HashMap<String, ArrayList<String>> mdMap = new HashMap<String, ArrayList<String>>();
		ArrayList<String> valuesList = null;

		int recordCount = 0;
		while (iterator.hasNext()) {
			ClientEntity mdEntity = iterator.next();
			List<ClientProperty> properties = mdEntity.getProperties();

			String listName = "";
			String listValue = "";
			int id = 0;

			for (ClientProperty property : properties) {
				if (FLD_ID.equalsIgnoreCase(property.getName())) {
					id = getIntValue(property, 0);
				}
				if (FLD_MD_LISTNAME.equalsIgnoreCase(property.getName())) {
					listName = property.getValue().toString();
				}
				if (FLD_MD_LISTVALUE.equalsIgnoreCase(property.getName())) {
					listValue = property.getValue().toString();
				}
			}

			valuesList = mdMap.get(listName);
			if (valuesList == null) {
				valuesList = new ArrayList<String>();
			}

			valuesList.add(listValue);
			mdMap.put(listName, valuesList);
			listName = "";
			listValue = "";

			logger.debug("Record Number {}. Transformed Master Data ID {}.", ++recordCount, id);
		}
		return mdMap;
	}

	public HashMap<String, Double> toStandardCostMap(ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator)
			throws Exception {

		HashMap<String, Double> costMap = new HashMap<String, Double>();

		int recordCount = 0;
		while (iterator.hasNext()) {
			ClientEntity mdEntity = iterator.next();
			List<ClientProperty> properties = mdEntity.getProperties();

			String costType = "";
			String locationCountry = "";
			String location = "";
			String grade = "";
			double monthlyCost=0d;

			int id = 0;

			for (ClientProperty property : properties) {
				if (FLD_ID.equalsIgnoreCase(property.getName())) {
					id = getIntValue(property, 0);
				}
				if (FLD_SD_COST_TYPE.equalsIgnoreCase(property.getName())) {
					costType = property.getValue().toString();
				}
				if (FLD_SD_LOCATION_COUNTRY.equalsIgnoreCase(property.getName())) {
					locationCountry = property.getValue().toString();
				}
				if (FLD_SD_LOCATION.equalsIgnoreCase(property.getName())) {
					location = property.getValue().toString();
				}
				if (FLD_SD_GRADE.equalsIgnoreCase(property.getName())) {
					grade = property.getValue().toString();
				}
				if (FLD_SD_MONTHLY_COST.equalsIgnoreCase(property.getName())) {
					monthlyCost = getDoubleValue(property, 0);
				}
			}
			
			StringBuffer key = new StringBuffer();
			key.append(costType);
			key.append(locationCountry);
			key.append(location);
			key.append(grade);
			key.trimToSize();

			costMap.put(key.toString(), monthlyCost);

			logger.debug("Record Number {}. Transformed Standard Cost ID {}.", ++recordCount, id);
		}
		
		return costMap;
	}

	public ArrayList<ParentCustomerVO> toRateCard(ClientEntity entity, ArrayList<ParentCustomerVO> pcustList)
			throws Exception {
		List<ClientProperty> properties = entity.getProperties();

		int parentCustomerId = 0;
		double rateCard = 0;
		String projectRole = null;
		String location = null;

		for (ClientProperty property : properties) {
			if (FLD_PARENT_CUSTOMER_ID.equalsIgnoreCase(property.getName())) {
				parentCustomerId = getIntValue(property, 0);
			}
			if (FLD_PROJECT_ROLE.equalsIgnoreCase(property.getName())) {
				projectRole = property.getValue().toString();
			}
			if (FLD_LOCATION.equalsIgnoreCase(property.getName())) {
				location = property.getValue().toString();
			}
			if (FLD_RATE_CARD.equalsIgnoreCase(property.getName())) {
				rateCard = getDoubleValue(property, 0);
			}
		}

		// check if the parent customer id exists, because the parent customer
		// list is a filtered list
		// that contains only the parent customers of the logged in DEM
		int index = pcustList.indexOf(new ParentCustomerVO().setId(parentCustomerId));
		if (index > -1) {
			ParentCustomerVO pcustVO = pcustList.get(index).addRateCard(projectRole, location, rateCard);
			pcustList.set(index, pcustVO);
		}

		return pcustList;
	}

	public MonkeyRolesVO toMonkeyRole(ClientEntity entity) throws Exception {
		List<ClientProperty> properties = entity.getProperties();

		MonkeyRolesVO roleVO = new MonkeyRolesVO();

		for (ClientProperty property : properties) {
			if (FLD_ID.equalsIgnoreCase(property.getName())) {
				roleVO.setId(getIntValue(property, 0));
			}
			if (FLD_ASSOCIATE_ID.equalsIgnoreCase(property.getName())) {
				roleVO.setAssociateId(getIntValue(property, 0));
			}
			if (FLD_ASSOCIATE_NAME.equalsIgnoreCase(property.getName())) {
				roleVO.setAssociateName(property.getValue().toString());
			}
			if (FLD_LOCATION.equalsIgnoreCase(property.getName())) {
				roleVO.setLocation(property.getValue().toString());
			}
			if (FLD_ROLE.equalsIgnoreCase(property.getName())) {
				roleVO.setRole(property.getValue().toString());
			}
			if (FLD_VERTICAL.equalsIgnoreCase(property.getName())) {
				roleVO.setVertical(property.getValue().toString());
			}
		}

		return roleVO;
	}
}
