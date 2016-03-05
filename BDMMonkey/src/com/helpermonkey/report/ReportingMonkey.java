package com.helpermonkey.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.olingo.client.api.communication.ODataClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.excel.ReportExcelHandler;
import com.helpermonkey.excel.ResourceExcelHandler;
import com.helpermonkey.olingo.AbstractOlingoMonkey;
import com.helpermonkey.olingo.CalculatedDataOlingoMonkey;
import com.helpermonkey.olingo.ResourceOlingoMonkey;
import com.helpermonkey.transformer.CalculatedNumbersTransformer;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.vo.CalculatedNumbersVO;
import com.helpermonkey.vo.ResourceVO;
import com.helpermonkey.vo.ProjectVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ReportingMonkey extends AbstractOlingoMonkey {
	private static final Logger logger = LoggerFactory.getLogger(ResourceOlingoMonkey.class);

	ResourceExcelHandler excelHandler = null;
	ReportExcelHandler rptExcelHandler = null;
	CalculatedNumbersTransformer calcTransformer = null;
	CalculatedDataOlingoMonkey calcOlingo = null;

	public ReportingMonkey() throws Exception {
		super();

		excelHandler = new ResourceExcelHandler();
		rptExcelHandler = new ReportExcelHandler();
		calcTransformer = new CalculatedNumbersTransformer(client.getObjectFactory());
		calcOlingo = new CalculatedDataOlingoMonkey();
	}

	public void init() throws Exception {
		rptExcelHandler.initialize();
	}

	public void createMonkeyReport() throws Exception {
		CalculatedNumbersVO calcVO = null;
		//ArrayList<RevenueItemVO> revenueList = StaticCache.cloneRevenueList();
		ArrayList<ProjectVO> revenueList = StaticCache.revenueList;
		
		// read the resource list from the Excel file
		List<ResourceVO> resourceList = excelHandler.readResourceSheetFromExcel();

		for (ResourceVO resourceVO : resourceList) {
			try {
				// this is an insert from resource entity itself, then we cant
				// expect calc vo to be present, so create a new one.
				calcVO = new CalculatedNumbersVO();
				calcVO.setRowNumber(resourceVO.getRowNumber());
				calcVO.setResourceItemVO(resourceVO);
				calcVO =  calcTransformer.calculateTheNumbers(calcVO);
				resourceVO.setCalculatedFieldsVO(calcVO);

				revenueList.get(revenueList.indexOf(new ProjectVO().setOpptyProjectName(resourceVO.getOpptyProjectName()))).addResource(resourceVO);
			} catch (Exception ex) {
				logger.error("Exception occurred in createMonkeyReport method.", ex);
				resourceVO.setChangeFlag(MonkeyConstants.CHANGE_FLAG_ERROR);
				String message = "CalcNumbers:" + ex.getMessage();

				if (ex instanceof ODataClientErrorException) {
					ODataClientErrorException odex = (ODataClientErrorException) ex;
					if (odex.getODataError() != null && odex.getODataError().getInnerError() != null) {
						message = odex.getODataError().getInnerError().toString();
						logger.error(message);
					}
				}
				resourceVO.getRscErrVO().setGeneralError(message);
				logger.error("Record Number{}, Error Message:", resourceVO.getRowNumber(), message);
			}
		}

		rptExcelHandler.writeReport(revenueList);
	}

}
