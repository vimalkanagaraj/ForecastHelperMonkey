package com.helpermonkey.olingo;

import java.net.URI;
import java.util.List;

import org.apache.olingo.client.api.communication.request.cud.ODataEntityCreateRequest;
import org.apache.olingo.client.api.communication.request.cud.ODataEntityUpdateRequest;
import org.apache.olingo.client.api.communication.response.ODataEntityCreateResponse;
import org.apache.olingo.client.api.communication.response.ODataEntityUpdateResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.excel.ResourceExcelHandler;
import com.helpermonkey.transformer.CalculatedNumbersTransformer;
import com.helpermonkey.util.StaticMasterData;
import com.helpermonkey.vo.CalculatedNumbersVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class CalculatedDataOlingoMonkey extends AbstractOlingoMonkey {
	private static final Logger logger = LoggerFactory.getLogger(CalculatedDataOlingoMonkey.class);

	ResourceExcelHandler excelHandler = null;
	CalculatedNumbersTransformer calcTransformer = null;

	public CalculatedDataOlingoMonkey() throws Exception {
		super();

		excelHandler = new ResourceExcelHandler();
		calcTransformer = new CalculatedNumbersTransformer(client.getObjectFactory());
	}

//	public void getCalculatedDataList() throws Exception {
//		URI absoluteUri = client.newURIBuilder(MonkeyConstants.CALCULATED_LIST_URL).build();
//		getCalculatedDataList(absoluteUri, false);
//	}

//	public void getCalculatedDataList(URI absoluteUri, boolean paginated) throws Exception {
//		ODataRetrieveResponse<ClientEntitySetIterator<ClientEntitySet, ClientEntity>> response = null;
//		ODataEntitySetIteratorRequest<ClientEntitySet, ClientEntity> request = null;
//
//		try {
//			request = client.getRetrieveRequestFactory().getEntitySetIteratorRequest(absoluteUri);
//			request.setAccept(ACCEPT_HEADER);
//			request.setContentType(CONTENT_TYPE_HEADER);
//
//			response = request.execute();
//			logger.info("HTTP Status Code {}", response.getStatusCode());
//
//			ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = response.getBody();
//
//			List<CalculatedNumbersVO> calcLLst = calcTransformer.toCalculatedVOList(iterator);
//			excelHandler.initialize();
//			excelHandler.writeNewCalculatedSheet(calcLLst, paginated);
//
//			// check if there is more records paginated by the server (for max
//			// threshold), if so server sends
//			// the pagination next page url in the response, get that and
//			// recurse again
//			URI nextPageUrl = iterator.getNext();
//
//			// go recursively and get the next set of paginated records from
//			// server and write to excel everytime
//			// we bring the data from the server.
//			// paginate and recurse nnly if the server indicates there is more
//			// records and provides the next page url...
//			if (nextPageUrl != null) {
//				getCalculatedDataList(nextPageUrl, true);
//			}
//
//		} catch (ODataClientErrorException ex) {
//			logger.error("Exception occurred:", ex);
//		}
//	}


	public void updateCalculatedData(ClientEntity calculatedEntity, CalculatedNumbersVO calcVO) throws Exception {
		// if id not equal to 0 then it means this record has be
		// updated in sharepoint, this is an update operation
		URI calculatedUri = client.newURIBuilder(MonkeyConstants.LIST_URL)
				.appendEntitySetSegment(StaticMasterData.CALCULATED_LIST_NAME)
				.appendKeySegment(new Integer(calcVO.getId())).build();

		// set the id in an URI form of the record that we plan to
		// update at the entry tag level
		calculatedEntity.setId(calculatedUri);

		// get the http request for update setting all the
		// required http headers etc
		ODataEntityUpdateRequest<ClientEntity> UpdateRequest = requestForUpdateRecord(calculatedUri, calculatedEntity);

		// fire the update query and wait in hope for the
		// result...
		ODataEntityUpdateResponse<ClientEntity> updateResult = UpdateRequest.execute();
		if (updateResult.getStatusCode() == 204) {
			logger.info("Updated Calculated Fields as well into SP - Row no {}", calcVO.getRowNumber());
		}
	}

	public void insertCalculatedData(ClientEntity calculatedEntity, CalculatedNumbersVO calcVO) throws Exception {
		URI resourceUri = client.newURIBuilder(MonkeyConstants.LIST_URL)
				.appendEntitySetSegment(StaticMasterData.CALCULATED_LIST_NAME).build();

		// get the http request for update setting all the required
		// http headers etc
		ODataEntityCreateRequest<ClientEntity> request = requestForNewInsert(resourceUri, calculatedEntity);
		// fire the update query and wait in hope for the result...
		ODataEntityCreateResponse<ClientEntity> result = request.execute();

		if (result.getStatusCode() == 201) {
			ClientEntity newEntity = result.getBody();
			List<ClientProperty> properties = newEntity.getProperties();

			for (ClientProperty property : properties) {
				if (CALC_DATA_ID.equalsIgnoreCase(property.getName())) {
					calcVO.setId(calcTransformer.getIntValue(property, 0));
					break;
				}
			}
			int statusCode = result.getStatusCode();

			if (statusCode == 201) {
				logger.info("Inserted Calculated Data as well: Row Number {}", calcVO.getRowNumber());
			}
		}
	}
}
