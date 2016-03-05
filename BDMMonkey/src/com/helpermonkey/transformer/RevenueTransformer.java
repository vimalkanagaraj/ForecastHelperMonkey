package com.helpermonkey.transformer;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.api.domain.ClientObjectFactory;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.vo.ProjectVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class RevenueTransformer extends AbstractTransformer {

	private static final Logger logger = LoggerFactory.getLogger(RevenueTransformer.class);

	public static final String FQNAME_REVENUE_LIST = "Microsoft.SharePoint.DataService.ProjectsItem";

	public static final String FLD_REMARKS = "Remarks";

	public static final String FLD_STATUS_VALUE = "StatusValue";

	public static final String FLD_CATEGORY_VALUE = "OpptyCategoryValue";

	public static final String FLD_PROJECT_TYPE_VALUE = "ProjectTypeValue";

	public static final String FLD_PARENT_CUSTOMER_ID = "ParentCustomerId";

	public static final String FLD_ESA_PROJECT_ID = "ESAProjectID";

	public static final String FLD_OPPTY_PROJECT_NAME = "OpptyProjectName";

	ClientObjectFactory objFactory = null;

	public RevenueTransformer() throws Exception {
		// objFactory = new RevenueOlingoMonkey().client.getObjectFactory();
		objFactory = ODataClientFactory.getClient().getObjectFactory();
	}

	public ProjectVO toRevenueItemVO(ClientEntity entity) throws Exception {
		ProjectVO revenueVO = new ProjectVO();
		List<ClientProperty> properties = entity.getProperties();

		for (ClientProperty property : properties) {
			if (FLD_ID.equalsIgnoreCase(property.getName())) {
				revenueVO.setId(getIntValue(property, 0));
			}
			if (FLD_OPPTY_PROJECT_NAME.equalsIgnoreCase(property.getName())) {
				revenueVO.setOpptyProjectName(property.getValue().toString());
			}
			if (FLD_ESA_PROJECT_ID.equalsIgnoreCase(property.getName())) {
				revenueVO.setESAProjectId(getLongValue(property, 0));
			}
			if (FLD_PARENT_CUSTOMER_ID.equalsIgnoreCase(property.getName())) {
				revenueVO.setParentCustomerName(StaticCache.getParentCustomerName(getIntValue(property, 0)));
			}
			if (FLD_PROJECT_TYPE_VALUE.equalsIgnoreCase(property.getName())) {
				revenueVO.setProjectTypeValue(property.getValue().toString());
			}
			if (FLD_REMARKS.equalsIgnoreCase(property.getName())) {
				revenueVO.setRemarks(property.getValue().toString());
			}
			if (FLD_STATUS_VALUE.equalsIgnoreCase(property.getName())) {
				revenueVO.setStatusValue(property.getValue().toString());
			}
			if (MonkeyConstants.FLD_DEM_OFFSHORE.equalsIgnoreCase(property.getName())) {
				revenueVO.setDemOffshoreId(getIntValue(property, 0));
			}
			if (MonkeyConstants.FLD_DEM_ONSITE.equalsIgnoreCase(property.getName())) {
				revenueVO.setDemOnsiteId(getIntValue(property, 0));
			}

		}

		return revenueVO;
	}

	public List<ProjectVO> toRevenueItemVO(ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator)
			throws Exception {
		List<ProjectVO> revenueList = new ArrayList<ProjectVO>();
		int recordCount = 0;
		while (iterator.hasNext()) {
			ClientEntity revenue = iterator.next();
			ProjectVO revVO = toRevenueItemVO(revenue);
			revenueList.add(revVO);
			logger.debug("Record Number {}. Transformed Revenue Item ID {}.", ++recordCount, revVO.getId());
		}
		return revenueList;
	}

	public ClientEntity toClientEntity(ProjectVO revenueVO) throws Exception {
		ClientEntity revenueEntity = objFactory.newEntity(new FullQualifiedName(FQNAME_REVENUE_LIST));

		// be careful with the objFactory.newPrimitiveValueBuilder() this is
		// actually a singleton and returns
		// a static instance, so everytime a new value needs to be set we need
		// to get a new instance...
		revenueEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_ID,
				objFactory.newPrimitiveValueBuilder().buildInt32(revenueVO.getId())));
		revenueEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_OPPTY_PROJECT_NAME,
				objFactory.newPrimitiveValueBuilder().buildString(revenueVO.getOpptyProjectName())));

		if(revenueVO.getESAProjectId() != 0){
			revenueEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_ESA_PROJECT_ID,
					objFactory.newPrimitiveValueBuilder().buildInt64(revenueVO.getESAProjectId())));
		}

		revenueEntity.getProperties()
				.add(objFactory.newPrimitiveProperty(FLD_PARENT_CUSTOMER_ID, objFactory.newPrimitiveValueBuilder()
						.buildInt32(StaticCache.getParentCustomerId(revenueVO.getParentCustomerName()))));
		revenueEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_PROJECT_TYPE_VALUE,
				objFactory.newPrimitiveValueBuilder().buildString(revenueVO.getProjectTypeValue())));
		revenueEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_STATUS_VALUE,
				objFactory.newPrimitiveValueBuilder().buildString(revenueVO.getStatusValue())));
		revenueEntity.getProperties().add(objFactory.newPrimitiveProperty(FLD_REMARKS,
				objFactory.newPrimitiveValueBuilder().buildString(revenueVO.getRemarks())));

		revenueEntity = setDemStamp(revenueVO.getParentCustomerName(), revenueEntity);

		return revenueEntity;
	}

	public List<ClientEntity> toClientEntity(List<ProjectVO> revenueList) throws Exception {
		List<ClientEntity> clientList = new ArrayList<ClientEntity>();

		for (ProjectVO revenueVO : revenueList) {
			clientList.add(toClientEntity(revenueVO));
		}

		return clientList;
	}

}
