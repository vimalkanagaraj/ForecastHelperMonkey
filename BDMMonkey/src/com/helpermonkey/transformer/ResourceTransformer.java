package com.helpermonkey.transformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.api.domain.ClientObjectFactory;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.vo.ResourceVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ResourceTransformer extends AbstractTransformer {

	private static final Logger logger = LoggerFactory.getLogger(ResourceTransformer.class);

	public static final String FQNAME_RESOURCE_ITEM = "Microsoft.SharePoint.DataService.ProjectResourcesItem";

	public static final String FLD_REMARKS = "Remarks";

	public static final String FLD_RESOURCE_STATUS_VALUE = "ResourceStatusValue";

	public static final String FLD_ASSOCIATE_NAME = "AssociateName";

	public static final String FLD_ASSOCIATE_ID = "AssociateID";

	public static final String FLD_SO_NUMBER = "SOUniqueNo";

	public static final String FLD_CONFIDENCE = "Confidence";

	public static final String FLD_BILLING_END_DATE = "BillingEndDate";

	public static final String FLD_BILLING_START_DATE = "BillingStartDate";

	public static final String FLD_REQUIRED_BY_DATE = "RequiredByDate";

	private static final String FLD_BILLABILITY = "Billability";
	
	private static final String FLD_FTE_COUNT = "FTECount";

	public static final String FLD_SKILLSET = "Skillset";

	public static final String FLD_COMPETENCY_VALUE = "CompetencyValue";

	public static final String FLD_RATECARD = "RateCard";
	
	public static final String FLD_LOCATION = "LocationValue";

	public static final String FLD_PROJECT_ROLE = "ProjectRoleValue";

	public static final String FLD_OPPTY_PROJECT_NAME = "OpptyProjectName";

	public static final String FLD_OPPTY_PROJECT_ID = "OpptyProjectId";

	public static final String FLD_PARENT_CUSTOMER_NAME = "ParentCustomerName";

	ClientObjectFactory objFactory = null;

	CalculatedNumbersTransformer calcTransformer = null;

	public ResourceTransformer(ClientObjectFactory objectFactory) throws Exception {
		this.objFactory = objectFactory;
		calcTransformer = new CalculatedNumbersTransformer(objectFactory);
	}

	public ResourceVO toResourceVO(ClientEntity entity) throws Exception {
		ResourceVO resourceItem = new ResourceVO();
		resourceItem.setRowNumber(1);
		List<ClientProperty> properties = entity.getProperties();

		for (ClientProperty property : properties) {
			if (FLD_ID.equalsIgnoreCase(property.getName())) {
				resourceItem.setId(getIntValue(property, 0));
			}
			if (FLD_PROJECT_ROLE.equalsIgnoreCase(property.getName())) {
				resourceItem.setGrade(property.getValue().toString());
			}
			if (FLD_OPPTY_PROJECT_NAME.equalsIgnoreCase(property.getName())) {
				resourceItem.setOpptyProjectName(property.getValue().toString());
			}
			if (FLD_PARENT_CUSTOMER_NAME.equalsIgnoreCase(property.getName())) {
				resourceItem.setParentCustomerName(property.getValue().toString());
			}
			if (FLD_LOCATION.equalsIgnoreCase(property.getName())) {
				resourceItem.setLocation(property.getValue().toString());
			}
			if (FLD_RATECARD.equalsIgnoreCase(property.getName())) {
				resourceItem.setRateCard(getDoubleValue(property, 0));
			}
			if (FLD_COMPETENCY_VALUE.equalsIgnoreCase(property.getName())) {
				resourceItem.setCompetency(property.getValue().toString());
			}
			if (FLD_SKILLSET.equalsIgnoreCase(property.getName())) {
				resourceItem.setSkillset(property.getValue().toString());
			}
			if (FLD_REQUIRED_BY_DATE.equalsIgnoreCase(property.getName())) {
				resourceItem.setRequiredByDate(property.getValue().toString());
			}
			if (FLD_BILLING_START_DATE.equalsIgnoreCase(property.getName())) {
				resourceItem.setBillingStartDate(property.getValue().toString());
			}
			if (FLD_BILLING_END_DATE.equalsIgnoreCase(property.getName())) {
				resourceItem.setBillingEndDate(property.getValue().toString());
			}
			if (FLD_CONFIDENCE.equalsIgnoreCase(property.getName())) {
				resourceItem.setConfidence(getDoubleValue(property, MonkeyConstants.DEFAULT_CONFIDENCE));
			}
			if (FLD_SO_NUMBER.equalsIgnoreCase(property.getName())) {
				resourceItem.setSOUniqueNumber(getIntValue(property, 0));
			}
			if (FLD_FTE_COUNT.equalsIgnoreCase(property.getName())) {
				resourceItem.setFTECount(getDoubleValue(property, 0));
			}
			if (FLD_BILLABILITY.equalsIgnoreCase(property.getName())) {
				resourceItem.setBillability(property.getValue().toString());
			}
			if (FLD_ASSOCIATE_ID.equalsIgnoreCase(property.getName())) {
				resourceItem.setAssociateId(getIntValue(property, 0));
			}
			if (FLD_ASSOCIATE_NAME.equalsIgnoreCase(property.getName())) {
				resourceItem.setAssociateName(property.getValue().toString());
			}
			if (FLD_REMARKS.equalsIgnoreCase(property.getName())) {
				resourceItem.setRemarks(property.getValue().toString());
			}
			if (FLD_RESOURCE_STATUS_VALUE.equalsIgnoreCase(property.getName())) {
				resourceItem.setResourceStatus(property.getValue().toString());
			}
		}

		return resourceItem;
	}

	public List<ResourceVO> toResourceVO(ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator)
			throws Exception {
		List<ResourceVO> resourceList = new ArrayList<ResourceVO>();

		int recordCount = 0;
		while (iterator.hasNext()) {
			ClientEntity resEntity = iterator.next();

			// first transform the client data pulled form SharePoint into
			// Resource ItemVO
			ResourceVO resourceVO = toResourceVO(resEntity);

			resourceList.add(resourceVO);
			logger.debug("Record Number {}. Transformed Resource Item ID {}.", ++recordCount, resourceVO.getId());
		}
		return resourceList;
	}

	public ClientEntity toClientEntity(ResourceVO resourceVO) throws Exception {
		ClientEntity resourceEntity = objFactory.newEntity(new FullQualifiedName(FQNAME_RESOURCE_ITEM));

		// if there is no id value in the excel row, it means this is a new
		// record for insert
		if (resourceVO.getId() == 0) {
			// insert
			// do nothing for now, basically we are not setting the id value
		} else {
			// update
			resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_ID,
					objFactory.newPrimitiveValueBuilder().buildInt32(resourceVO.getId())));
		}
		// be careful with the objFactory.newPrimitiveValueBuilder() this is
		// actually a singleton and returns
		// a static instance, so everytime a new value needs to be set we need
		// to get a new instance...
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_OPPTY_PROJECT_ID,
				objFactory.newPrimitiveValueBuilder().buildInt32(resourceVO.getOpptyProjectId())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_OPPTY_PROJECT_NAME,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getOpptyProjectName())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_PARENT_CUSTOMER_NAME,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getParentCustomerName())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_LOCATION,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getLocation())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_RATECARD,
				objFactory.newPrimitiveValueBuilder().buildDouble(resourceVO.getRateCard())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_PROJECT_ROLE,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getGrade())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_COMPETENCY_VALUE,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getCompetency())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_SKILLSET,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getSkillset())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_REQUIRED_BY_DATE,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getRequiredByDate())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_BILLING_START_DATE,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getBillingStartDate())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_BILLING_END_DATE,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getBillingEndDate())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_CONFIDENCE,
				objFactory.newPrimitiveValueBuilder().buildDouble(resourceVO.getConfidence())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_SO_NUMBER,
				objFactory.newPrimitiveValueBuilder().buildInt32(resourceVO.getSOUniqueNumber())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_FTE_COUNT,
				objFactory.newPrimitiveValueBuilder().buildDouble(resourceVO.getFTECount())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_BILLABILITY,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getBillability())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_ASSOCIATE_ID,
				objFactory.newPrimitiveValueBuilder().buildInt32(resourceVO.getAssociateId())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_ASSOCIATE_NAME,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getAssociateName())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_REMARKS,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getRemarks())));
		resourceEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_RESOURCE_STATUS_VALUE,
				objFactory.newPrimitiveValueBuilder().buildString(resourceVO.getResourceStatus())));
		
		resourceEntity = setDemStamp(resourceVO.getParentCustomerName(), resourceEntity);

		return resourceEntity;
	}

	public List<ClientEntity> toClientEntity(List<ResourceVO> resourceList) throws Exception {
		List<ClientEntity> clientList = new ArrayList<ClientEntity>();

		for (ResourceVO resourceItem : resourceList) {
			clientList.add(toClientEntity(resourceItem));
		}

		return clientList;
	}

}
