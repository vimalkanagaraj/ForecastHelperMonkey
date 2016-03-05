package com.helpermonkey.transformer;

import java.net.URI;

import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientLink;
import org.apache.olingo.client.api.domain.ClientObjectFactory;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.apache.olingo.client.core.ODataClientFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticCache;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public abstract class AbstractTransformer {

	ClientObjectFactory objFactory = null;
	
	ODataClient client = null;
	
	public static final String FLD_ID = "Id";
	
	public AbstractTransformer() throws Exception {
		client = ODataClientFactory.getClient();
		objFactory = client.getObjectFactory();
	}

	public URI prepareLinks(ClientEntity updateEntity, URI uri, String listName, int id) throws Exception {
		ClientLink link = objFactory.newEntityNavigationLink(listName, uri);
		updateEntity.getNavigationLinks().add(link);

		URI uriWithId = client.newURIBuilder(MonkeyConstants.LIST_URL)
				.appendEntitySetSegment(listName).appendKeySegment(new Integer(id)).build();
		return uriWithId;
	}
	
	public int getIntValue(ClientProperty property, int defaultValue) throws Exception{
		if (property.getPrimitiveValue() == null) {
			return defaultValue;
		}
		
		return property.getPrimitiveValue().toCastValue(Integer.class).intValue();
	}
	
	public long getLongValue(ClientProperty property, int defaultValue) throws Exception{
		if (property.getPrimitiveValue() == null) {
			return defaultValue;
		}
		
		String strVal = property.getValue().toString(); 
		
		if(strVal == null || strVal.length() < 1){
			return defaultValue;
		}
		
		return  Double.valueOf(strVal).longValue();
	}


	public double getDoubleValue(ClientProperty property, double defaultValue) throws Exception{
		if (property.getPrimitiveValue() == null) {
			return defaultValue;
		}
		
		return property.getPrimitiveValue().toCastValue(Double.class).doubleValue();
	}
	
	public ClientEntity setDemStamp(String customerName, ClientEntity entity) throws Exception{
		//set the dem data
		entity.getProperties().add(objFactory.newPrimitiveProperty(MonkeyConstants.FLD_DEM_OFFSHORE,
				objFactory.newPrimitiveValueBuilder().buildInt32(StaticCache.getOffshoreDEMId(customerName))));
		entity.getProperties().add(objFactory.newPrimitiveProperty(MonkeyConstants.FLD_DEM_ONSITE,
				objFactory.newPrimitiveValueBuilder().buildInt32(StaticCache.getOnsiteDEMId(customerName))));
		
		return entity;
	}
	
	//	public URI prepareLinks(ClientEntity updateEntity) throws Exception {
//		URI revenueUri = RevenueOlingoMonkey.client.newURIBuilder(MonkeyConstants.LIST_URL)
//				.appendEntitySetSegment("Revenue").appendKeySegment(new Integer(809)).build();
//		return revenueUri;
//	}
}
