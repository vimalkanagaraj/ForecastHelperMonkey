package com.helpermonkey.olingo;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.transformer.MasterDataTransformer;
import com.helpermonkey.util.StaticMasterData;
import com.helpermonkey.vo.MonkeyRolesVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MasterDataOlingoMonkey extends AbstractOlingoMonkey {

	MasterDataTransformer masterTransformer = null;

	private static final Logger logger = LoggerFactory.getLogger(ParentCustomerOlingoMonkey.class);

	public MasterDataOlingoMonkey() throws Exception {
		super();

		masterTransformer = new MasterDataTransformer();
	}

	public MonkeyRolesVO getSingleMonkeyRole(int monkeyRoleId) throws Exception {
		URI monkeyRoleUri = client.newURIBuilder(MonkeyConstants.LIST_URL)
				.appendEntitySetSegment(StaticMasterData.MONKEY_ROLES_LIST_NAME).appendKeySegment(monkeyRoleId).build();

		MonkeyRolesVO roleVO = null;

		ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(monkeyRoleUri);

		while (iterator.hasNext()) {
			ClientEntity monkeyRoleEntity = iterator.next();
			roleVO = masterTransformer.toMonkeyRole(monkeyRoleEntity);
		}
		return roleVO;
	}
	
	public ArrayList<MonkeyRolesVO> getMonkeyRoles() throws Exception {
		ArrayList<MonkeyRolesVO> rolesList = new ArrayList<MonkeyRolesVO>();
		
		URI monkeyRoleUri = client.newURIBuilder(MonkeyConstants.LIST_URL)
				.appendEntitySetSegment(StaticMasterData.MONKEY_ROLES_LIST_NAME).build();

		ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(monkeyRoleUri);

		while (iterator.hasNext()) {
			ClientEntity monkeyRoleEntity = iterator.next();
			rolesList.add(masterTransformer.toMonkeyRole(monkeyRoleEntity));
		}
		return rolesList;
	}

	public HashMap<String, ArrayList<String>> getMasterDataMap() throws Exception {
		URI masterDataUrl = client.newURIBuilder(MonkeyConstants.LIST_URL)
				.appendEntitySetSegment(StaticMasterData.MASTERDATA_LIST_NAME).build();

		ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(masterDataUrl);

		HashMap<String, ArrayList<String>> mdMap = masterTransformer.toMasterDataMap(iterator);

		logger.debug("Completed Retrieving MasterData list from SP");

		return mdMap;
	}
	
	public HashMap<String, Double> getStandardCostMap() throws Exception {
		URI masterDataUrl = client.newURIBuilder(MonkeyConstants.LIST_URL)
				.appendEntitySetSegment(StaticMasterData.STD_COST_LIST_NAME).build();

		ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator = execute(masterDataUrl);

		HashMap<String, Double> mdMap = masterTransformer.toStandardCostMap(iterator);

		logger.debug("Completed Retrieving Standard Costb from SP");

		return mdMap;
	}

}
