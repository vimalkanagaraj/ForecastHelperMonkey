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
import com.helpermonkey.excel.ProjectExcelHandler;
import com.helpermonkey.transformer.RevenueTransformer;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.vo.ProjectVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class RevenueOlingoMonkey extends AbstractOlingoMonkey {

	RevenueTransformer revenueTransformer = null;

	ProjectExcelHandler excelHandler = null;

	private static final Logger logger = LoggerFactory.getLogger(RevenueOlingoMonkey.class);

	public RevenueOlingoMonkey() throws Exception {
		super();

		revenueTransformer = new RevenueTransformer();
		excelHandler = new ProjectExcelHandler();
	}

	public void getRevenueListData() throws Exception {
		getRevenueListData(createURIWithDEMFilter(MonkeyConstants.REVENUE_LIST_URL), false);
	}

	public void getRevenueListData(URI absoluteUri, boolean paginated) throws Exception {

		try {
			ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(absoluteUri);

			List<ProjectVO> revenueList = revenueTransformer.toRevenueItemVO(iterator);
			
			logger.debug("Revenue List from Sharepoint:{}", revenueList);

			excelHandler.writeRevenueVOToExcel(revenueList, paginated, true);

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
				getRevenueListData(nextPageUrl, true);
			}

		} catch (ODataClientErrorException ex) {
			logger.error("Exception occurred in getRevenueListData() ", ex);
		}
	}

	public void updateRevenueRecord() throws Exception {
		int recordNo = 0;
		boolean updateOccurred = false;
		ArrayList<ProjectVO> updatedRevenueList = new ArrayList<ProjectVO>();
		
		// Be careful here, as we are directly using/accessing the static
		// Revenue List
		ArrayList<ProjectVO> list = StaticCache.revenueList; 
		for (ProjectVO revVO : list) {
			logger.debug("I am on record no {}, change flag is {}", ++recordNo, revVO.getChangeFlag());

			try {
				if (MonkeyConstants.CHANGE_FLAG_UPDATE.equalsIgnoreCase(revVO.getChangeFlag())) {
					updateOccurred = true;
					// set all the properties from excel into the client entity
					// object
					// which
					// will be used for update operation
					ClientEntity revEntity = revenueTransformer.toClientEntity(revVO);

					// if id == 0 then it means this record has be
					// inserted/created
					// in sharepoint, this is a create operation
					if (revVO.getId() == 0) {

						URI insertURI = client.newURIBuilder(MonkeyConstants.REVENUE_LIST_URL).build();

						// get the http request for update setting all the
						// required http headers etc
						ODataEntityCreateRequest<ClientEntity> request = requestForNewInsert(insertURI, revEntity);

						// fire the update query and wait in hope for the
						// result..
						ODataEntityCreateResponse<ClientEntity> result = request.execute();

						if (result.getStatusCode() == 201) {
							ClientEntity newEntity = result.getBody();
							List<ClientProperty> properties = newEntity.getProperties();

							for (ClientProperty property : properties) {
								if (RESOURCE_ID.equalsIgnoreCase(property.getName())) {
									revVO.setId(revenueTransformer.getIntValue(property, 0));
									revVO.setChangeFlag(MonkeyConstants.CHANGE_FLAG_SUCCESS);
									break;
								}
							}

							logger.info("Inserted Project, excel row no: {}, generated SP id:{}" + revVO.getRowNumber(), revVO.getId());
						} else {
							revVO.getPrjErrVO().setGeneralError("HTTP Code:" + result.getStatusCode() + ". Description:"
									+ result.getStatusMessage());
						}
					} else {
						// if id not equal to 0 then it means this record has be
						// updated in sharepoint, this is an update operation
						URI revenueUri = client.newURIBuilder(MonkeyConstants.REVENUE_LIST_URL)
								.appendKeySegment(revVO.getId()).build();

						// set the id in an URI form of the record that we plan
						// to update at the entry tag level
						revEntity.setId(revenueUri);

						// get the http request for update setting all the
						// required http headers etc
						ODataEntityUpdateRequest<ClientEntity> updateRequest = requestForUpdateRecord(revenueUri,
								revEntity);

						// fire the update query and wait in hope for the
						// result...
						ODataEntityUpdateResponse<ClientEntity> updateResult = updateRequest.execute();
						if (updateResult.getStatusCode() == 204) {
							// if you are here then bingo the update should have
							// gone fine...
							// now write the calculated fileds, change_flag and
							// stuff back into the excel now relax..
							// S means SUCCESS
							revVO.setChangeFlag(MonkeyConstants.CHANGE_FLAG_SUCCESS);
							logger.info("Updated Project, project id: {}, excel row no: {}", revVO.getId(), revVO.getRowNumber());
						} else {
							revVO.getPrjErrVO().setGeneralError("HTTP Code:" + updateResult.getStatusCode() + ". Description:"
									+ updateResult.getStatusMessage());
						}
					}

					// insert or update, write the data back into excel such
					// that the new id info, calculated fields data are updated
					updatedRevenueList.add(revVO);
				}
			} catch (Exception ex) {
				logger.error("Exception occurred in updateRevenueRecord() ", ex);
				revVO.setChangeFlag(MonkeyConstants.CHANGE_FLAG_ERROR);
				String message = ex.getMessage();

				if(ex instanceof ODataClientErrorException){
					ODataClientErrorException odex = (ODataClientErrorException) ex;
					if(odex.getODataError() != null && odex.getODataError().getInnerError() != null){
						message = odex.getODataError().getInnerError().toString();
						logger.error(message);
					}
				}

				revVO.getPrjErrVO().setGeneralError(message);
				logger.error("excel row no:{}, resource id:{} Error Message:", revVO.getRowNumber(), revVO.getId(), message);
				updatedRevenueList.add(revVO);
			}
		}
		
		if(updateOccurred){
			excelHandler.writeRevenueVOToExcel(updatedRevenueList, false, false);
		}
	}

}
