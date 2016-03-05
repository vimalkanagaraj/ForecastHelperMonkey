package com.helpermonkey.olingo;

import java.net.URI;
import java.util.ArrayList;

import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.cud.ODataEntityCreateRequest;
import org.apache.olingo.client.api.communication.request.cud.ODataEntityUpdateRequest;
import org.apache.olingo.client.api.communication.request.cud.UpdateType;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetIteratorRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.core.ODataClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticCache;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public abstract class AbstractOlingoMonkey {

	public static final String ACCEPT_ATOM = "application/atom+xml";
	public static final String OVERWRITE_IF_FOUND_HEADER = "*";
	// public static final String CONTENT_TYPE_HEADER =
	// "application/atom+xml;charset=utf-8";
	public static final String CONTENT_TYPE_HEADER = "application/atom+xml";
	public static final String ACCEPT_HEADER = "text/html, application/atom+xml, application/xml;";
	public static final String RESOURCE_ID = "Id";
	public static final String CALC_DATA_ID = "Id";

	private static final Logger logger = LoggerFactory.getLogger(AbstractOlingoMonkey.class);

	public ODataClient client = null;

	public AbstractOlingoMonkey() throws Exception {
		if (client == null) {
			client = ODataClientFactory.getClient();
			// Initialize the odata client with our custom http client factory,
			// this
			// is the magic piece to make cookies work
			client.getConfiguration().setHttpClientFactory(new CookieBasedHttpClientFactory());
			
			if(StaticCache.loggedInUser == null){
				loginIntoSP();
			}
		}
	}

	public void loginIntoSP() throws Exception {
		ODataEntitySetIteratorRequest<ClientEntitySet, ClientEntity> request = null;
		request = client.getRetrieveRequestFactory()
				.getEntitySetIteratorRequest(new URI(MonkeyConstants.RESOURCE_LIST_URL));
		request.setAccept(ACCEPT_HEADER);
		request.setContentType(CONTENT_TYPE_HEADER);
		ODataRetrieveResponse<ClientEntitySetIterator<ClientEntitySet, ClientEntity>> response = request.execute();
		logger.debug("Login HTTP Status Code:" + response.getStatusCode());
	}

	public void getListMetadata() throws Exception {

	}

	// public void getResourceListData() throws Exception;
	// public void getResourceListData(URI absoluteUri, boolean paginated)
	// throws Exception;
	// public void updateResourceRecord() throws Exception ;

	public ODataEntityUpdateRequest<ClientEntity> requestForUpdateRecord(URI uri, ClientEntity entity) {
		ODataEntityUpdateRequest<ClientEntity> request = client.getCUDRequestFactory().getEntityUpdateRequest(uri,
				UpdateType.MERGE, entity);
		request.setAccept(ACCEPT_HEADER);
		request.setContentType(CONTENT_TYPE_HEADER);
		request.setIfMatch(OVERWRITE_IF_FOUND_HEADER);
		return request;
	}

	public ODataEntityCreateRequest<ClientEntity> requestForNewInsert(URI uri, ClientEntity entity) {
		ODataEntityCreateRequest<ClientEntity> request = client.getCUDRequestFactory().getEntityCreateRequest(uri,
				entity);
		request.setAccept(ACCEPT_ATOM);
		request.setContentType(CONTENT_TYPE_HEADER);
		// request.setXHTTPMethod("PUT");
		return request;
	}

	public ClientEntitySetIterator<ClientEntitySet, ClientEntity> execute(URI absoluteUri) {
		ODataRetrieveResponse<ClientEntitySetIterator<ClientEntitySet, ClientEntity>> response = null;
		ODataEntitySetIteratorRequest<ClientEntitySet, ClientEntity> request = null;

		request = client.getRetrieveRequestFactory().getEntitySetIteratorRequest(absoluteUri);
		request.setAccept(ACCEPT_HEADER);
		request.setContentType(CONTENT_TYPE_HEADER);

		logger.info("About to execute HTTP path {}, filter {}", absoluteUri.getPath(), absoluteUri.getQuery());
		response = request.execute();
		logger.info("HTTP Status Code {}", response.getStatusCode());

		return response.getBody();
	}

	public URI createURIForParentCustomer(String absoluteUri) throws Exception {
		// create this dem level filter only if the role is DEM, if the role is
		// DBP/OPS dont filter at all
		if (MonkeyConstants.ROLE_DEM.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole())) {
			if (StaticCache.loggedInUser.isOnsite()) {
				return client.newURIBuilder(absoluteUri).filter("DEMOnsiteId eq " + StaticCache.loggedInUser.getId())
						.build();
			} else {
				return client.newURIBuilder(absoluteUri).filter("DEMOffshoreId eq " + StaticCache.loggedInUser.getId())
						.build();
			}
		}

		if (MonkeyConstants.ROLE_DBP.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole())
				|| MonkeyConstants.ROLE_OPS.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole())) {
			return client.newURIBuilder(absoluteUri)
					.filter("VerticalValue eq '" + StaticCache.loggedInUser.getVertical() + "'").build();
		}
		return client.newURIBuilder(absoluteUri).build();
	}

	public URI createURIWithDEMFilter(String absoluteUri) throws Exception {
		// create this dem level filter only if the role is DEM, if the role is
		// DBP/OPS dont filter at all
		if (MonkeyConstants.ROLE_DEM.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole())) {
			if (StaticCache.loggedInUser.isOnsite()) {
				return client.newURIBuilder(absoluteUri)
						.filter("DEMOnsiteId eq " + StaticCache.loggedInUser.getId()).build();
			} else {
				return client.newURIBuilder(absoluteUri)
						.filter("DEMOffshoreId eq " + StaticCache.loggedInUser.getId()).build();
			}
		}
		
		if (MonkeyConstants.ROLE_DBP.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole()) || 
				MonkeyConstants.ROLE_OPS.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole())) {
			String filterCriteria = getDEMListFilterCriteria();
			return client.newURIBuilder(absoluteUri).filter(filterCriteria).build();
		}

		return client.newURIBuilder(absoluteUri).build();
	}

	protected String getDEMListFilterCriteria() {
		ArrayList<Integer> demList = StaticCache.getDEMForLoggedInUserVertical();
		
		String filterCriteria = "";
		int size = demList.size();
		for (int i=0;i<size;i++) {
			Integer demId = demList.get(i);
			
			if (StaticCache.loggedInUser.isOnsite()) {
				filterCriteria += "DEMOnsiteId eq " + demId; 
			}else{
				filterCriteria += "DEMOffshoreId eq " + demId; 
			}

			if(i<(size-1)){
				filterCriteria += " or ";
			}
		}
		return filterCriteria;
	}
	

	public URI createURIWithDEMFilter(String absoluteUri, int id) throws Exception {
		if (MonkeyConstants.ROLE_DEM.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole())) {
			if (StaticCache.loggedInUser.isOnsite()) {
				return client.newURIBuilder(absoluteUri).appendKeySegment(id)
						.filter("DEMOnsiteId eq " + StaticCache.loggedInUser.getId()).build();
			} else {
				return client.newURIBuilder(absoluteUri).appendKeySegment(id)
						.filter("DEMOffshoreId eq " + StaticCache.loggedInUser.getId()).build();
			}
		}
		
		if (MonkeyConstants.ROLE_DBP.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole()) || 
				MonkeyConstants.ROLE_OPS.equalsIgnoreCase(StaticCache.getLoggedInDEM().getRole())) {
			String filterCriteria = getDEMListFilterCriteria();
			return client.newURIBuilder(absoluteUri).appendKeySegment(id).filter(filterCriteria).build();
		}

		return client.newURIBuilder(absoluteUri).build();
	}

	// will be supported only in next version of NumbersMonkey
	public void deleteResourceRecord() {

	}
}
