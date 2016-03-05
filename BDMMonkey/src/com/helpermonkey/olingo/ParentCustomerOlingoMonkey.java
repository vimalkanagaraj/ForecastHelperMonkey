package com.helpermonkey.olingo;

import java.net.URI;
import java.util.ArrayList;

import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.transformer.MasterDataTransformer;
import com.helpermonkey.vo.ParentCustomerVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ParentCustomerOlingoMonkey extends AbstractOlingoMonkey {

	MasterDataTransformer masterTransformer = null;

	private static final Logger logger = LoggerFactory.getLogger(ParentCustomerOlingoMonkey.class);

	public ParentCustomerOlingoMonkey() throws Exception {
		super();

		masterTransformer = new MasterDataTransformer();
	}

	public ArrayList<ParentCustomerVO> getParentCustomerList() throws Exception {
		URI uri = createURIForParentCustomer(MonkeyConstants.PARENTCUSTOMER_LIST_URL);

		ArrayList<ParentCustomerVO> pcustList = getParentCustomerList(uri, false);

		logger.debug("Completed Retrieving the parent customer list from SP");

		return pcustList;
	}

//	public ArrayList<ParentCustomerVO> getParentCustomerListWithRateCard() throws Exception {
//
//		ArrayList<ParentCustomerVO> pcustList = getParentCustomerListAndWriteToExcel();
//
//		URI rateCardUri = client.newURIBuilder(MonkeyConstants.RATECARD_LIST_URL).build();
//		pcustList = getRateCardList(rateCardUri, false, pcustList);
//
//		logger.debug("Completed Retrieving the rate card list from SP");
//
//		return pcustList;
//	}

	public ArrayList<ParentCustomerVO> getRateCardList(URI rateCardUri, boolean paginated,
			ArrayList<ParentCustomerVO> pcustList) throws Exception {

		ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(rateCardUri);

		while (iterator.hasNext()) {
			ClientEntity rateCardEntity = iterator.next();
			pcustList = masterTransformer.toRateCard(rateCardEntity, pcustList);
		}

		// check if there is more records paginated by the server and recurse
		// again
		URI nextPageUrl = iterator.getNext();

		// go recursively and get the next set of paginated records from
		// server and write to excel everytime
		// we bring the data from the server.
		// paginate and recurse nnly if the server indicates there is more
		// records and provides the next page url...
		if (nextPageUrl != null) {
			pcustList = getRateCardList(nextPageUrl, true, pcustList);
		}

		return pcustList;
	}

	public ArrayList<ParentCustomerVO> getParentCustomerList(URI absoluteUri, boolean paginated) throws Exception {

		ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(absoluteUri);

		ArrayList<ParentCustomerVO> pcustList = masterTransformer.toParentCustomerVO(iterator);

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
			pcustList.addAll(getParentCustomerList(nextPageUrl, true));
		}

		return pcustList;
	}

}
