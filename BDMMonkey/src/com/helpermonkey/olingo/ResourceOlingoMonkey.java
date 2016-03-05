package com.helpermonkey.olingo;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.communication.ODataClientErrorException;
import org.apache.olingo.client.api.communication.request.cud.ODataEntityCreateRequest;
import org.apache.olingo.client.api.communication.request.cud.ODataEntityUpdateRequest;
import org.apache.olingo.client.api.communication.response.ODataEntityCreateResponse;
import org.apache.olingo.client.api.communication.response.ODataEntityUpdateResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.excel.ResourceExcelHandler;
import com.helpermonkey.transformer.ResourceTransformer;
import com.helpermonkey.util.StaticMasterData;
import com.helpermonkey.vo.ResourceVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ResourceOlingoMonkey extends AbstractOlingoMonkey {
	private static final Logger logger = LoggerFactory.getLogger(ResourceOlingoMonkey.class);

	ResourceTransformer resTransformer = null;
	ResourceExcelHandler excelHandler = null;

	public ResourceOlingoMonkey() throws Exception {
		super();

		resTransformer = new ResourceTransformer(client.getObjectFactory());
		excelHandler = new ResourceExcelHandler();
	}

	public void getResourceListData() throws Exception {
		URI absoluteUri = createURIWithDEMFilter(MonkeyConstants.RESOURCE_LIST_URL);
		getResourceListData(absoluteUri, false);
	}

	public void getResourceListData(URI absoluteUri, boolean paginated) throws Exception {

		try {
			ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(absoluteUri);

			List<ResourceVO> resourceList = resTransformer.toResourceVO(iterator);
			excelHandler.writeNewResourceSheet(resourceList, paginated, false);

			// check if there is more records paginated by the server (for max
			// threshold), if so server sends
			// the pagination next page url in the response, get that and
			// recurse again
			URI nextPageUrl = iterator.getNext();

			// go recursively and get the next set of paginated records from
			// server and write to excel everytime
			// we bring the data from the server.
			// paginate and recurse nnly if the server indicates there is more
			// records and provides the next page url...
			if (nextPageUrl != null) {
				getResourceListData(nextPageUrl, true);
			}

		} catch (ODataClientErrorException ex) {
			logger.error("Exception occurred:", ex);
		}
	}

	public void updateResourceRecord() throws Exception {
		List<ResourceVO> subList = new ArrayList<ResourceVO>();

		// read the resource list from the Excel file
		List<ResourceVO> resourceList = excelHandler.readResourceSheetFromExcel();

		// This while loop is for performance improvement, so that garbage
		// collection can happen for the localized objects.
		int subListSize = 300;
		int totalRecords = resourceList.size();
		int initialIndex = 0;
		int endIndex = 301;
		if(totalRecords < 301){
			endIndex = totalRecords;
		}
		while (initialIndex < totalRecords) {
			subList = resourceList.subList(initialIndex, endIndex);
			if (updateResourceSubList(subList)) {
				// write the error details / id etc into excel only if any
				// update was even required
				excelHandler.writeNewResourceSheet(subList, false, true);
			}

			initialIndex = endIndex;
			endIndex = endIndex + subListSize;
			if (endIndex > totalRecords) {
				endIndex = totalRecords;
			}
		}
	}

	private boolean updateResourceSubList(List<ResourceVO> resourceList) {
		boolean updateOccurred = false;
		for (ResourceVO resourceVO : resourceList) {
			try {
				// if change flag is == y then this records needs to be posted
				// to sharepoint
				if (MonkeyConstants.CHANGE_FLAG_UPDATE.equalsIgnoreCase(resourceVO.getChangeFlag())) {
					updateOccurred = true;
					// set all the properties from excel into the client entity
					// object which will be used for update operation
					ClientEntity resourceEntity = resTransformer.toClientEntity(resourceVO);

					// if id == 0 then it means this record has be
					// inserted/created
					// in sharepoint, this is a create operation
					if (resourceVO.getId() == 0) {
						URI resourceUri = client.newURIBuilder(MonkeyConstants.LIST_URL)
								.appendEntitySetSegment(StaticMasterData.RESOURCE_LIST_NAME).build();

						// get the http request for update setting all the
						// required
						// http headers etc
						ODataEntityCreateRequest<ClientEntity> request = requestForNewInsert(resourceUri,
								resourceEntity);
						// fire the update query and wait in hope for the
						// result...
						ODataEntityCreateResponse<ClientEntity> result = request.execute();

						if (result.getStatusCode() == 201) {
							ClientEntity newEntity = result.getBody();
							List<ClientProperty> properties = newEntity.getProperties();

							for (ClientProperty property : properties) {
								if (RESOURCE_ID.equalsIgnoreCase(property.getName())) {
									resourceVO.setId(resTransformer.getIntValue(property, 0));
									resourceVO.setChangeFlag(MonkeyConstants.CHANGE_FLAG_SUCCESS);
									break;
								}
							}
							logger.info("Inserted Resource, excel row no: {}, generated SP id:{}", resourceVO.getRowNumber(), resourceVO.getId());
						}
					} else {
						// if id not equal to 0 then it means this record has be
						// updated in sharepoint, this is an update operation

						URI resourceUri = client.newURIBuilder(MonkeyConstants.LIST_URL)
								.appendEntitySetSegment(StaticMasterData.RESOURCE_LIST_NAME)
								.appendKeySegment(new Integer(resourceVO.getId())).build();

						// set the id in an URI form of the record that we plan
						// to
						// update at the entry tag level
						resourceEntity.setId(resourceUri);

						// get the http request for update setting all the
						// required http headers etc
						ODataEntityUpdateRequest<ClientEntity> UpdateRequest = requestForUpdateRecord(resourceUri,
								resourceEntity);

						// fire the update query and wait in hope for the
						// result...
						ODataEntityUpdateResponse<ClientEntity> updateResult = UpdateRequest.execute();
						if (updateResult.getStatusCode() == 204) {
							// if you are here then bingo the update should have
							// gone fine...
							// now write the change_flag and
							// stuff back into the excel now relax..
							// S means SUCCESS
							resourceVO.setChangeFlag(MonkeyConstants.CHANGE_FLAG_SUCCESS);
							logger.info("Updated Resource, resource id: {}, excel row no: {}", resourceVO.getId(), resourceVO.getRowNumber());

						}
					}
				}
			} catch (Exception ex) {
				logger.error("Exception occurred in update resource method.", ex);
				resourceVO.setChangeFlag(MonkeyConstants.CHANGE_FLAG_ERROR);
				String message = ex.getMessage();

				if (ex instanceof ODataClientErrorException) {
					ODataClientErrorException odex = (ODataClientErrorException) ex;
					if (odex.getODataError() != null && odex.getODataError().getInnerError() != null) {
						message = odex.getODataError().getInnerError().toString();
						logger.error(message);
					}
				}
				resourceVO.getRscErrVO().setGeneralError(message);
				logger.error("excel row no:{}, resource id:{} Error Message:", resourceVO.getRowNumber(), resourceVO.getId(), message);
			}
		}
		return updateOccurred;
	}
}
