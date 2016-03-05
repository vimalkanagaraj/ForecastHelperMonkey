package com.helpermonkey.excel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.vo.CalculatedNumbersVO;
import com.helpermonkey.vo.ResourceVO;
import com.helpermonkey.vo.ProjectVO;

public class ReportExcelHandler extends AbstractExcelHandler {

	private static final Logger logger = LoggerFactory.getLogger(ReportExcelHandler.class);

	public ReportExcelHandler() throws Exception {
		initialize();
	}

	public void initialize() throws Exception {
	}

	public void writeHeaderRow(Sheet calcSheet) throws Exception {
		int columnCount = -1;

		boolean skipWritingHeader = false;
		Iterator<Row> iterator = calcSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			// consider first row as the header row and check if header is
			// already present
			while (cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				// if there is a value that already exists, then we should not
				// write the header again
				if (EXFLD_ERROR_DETAILS.equalsIgnoreCase(getStringValue(nextCell))) {
					// skip writing the header.
					skipWritingHeader = true;
					break;
				}
			}
			if (skipWritingHeader) {
				break;
			}
		}

		if (!skipWritingHeader) {
			Row row = calcSheet.createRow(0);

			row.createCell(++columnCount).setCellValue(EXFLD_ERROR_DETAILS);
			row.createCell(++columnCount).setCellValue(MonkeyConstants.FLD_DEM_OFFSHORE);
			row.createCell(++columnCount).setCellValue(MonkeyConstants.FLD_DEM_ONSITE);

			row.createCell(++columnCount).setCellValue(EXFLD_RESOURCE_ALLOCATION_ID);
			row.createCell(++columnCount).setCellValue(EXFLD_PARENT_CUSTOMER_NAME);
			row.createCell(++columnCount).setCellValue(EXFLD_OPPTY_PROJECT_NAME);
			row.createCell(++columnCount).setCellValue(EXFLD_ESA_PROJECT_ID);
			row.createCell(++columnCount).setCellValue(EXFLD_PROJECT_TYPE);
			row.createCell(++columnCount).setCellValue(EXFLD_STATUS);
			row.createCell(++columnCount).setCellValue(EXFLD_REMARKS);

			row.createCell(++columnCount).setCellValue(EXFLD_SO_NUMBER);
			row.createCell(++columnCount).setCellValue(EXFLD_GRADE);
			row.createCell(++columnCount).setCellValue(EXFLD_LOCATION);
			row.createCell(++columnCount).setCellValue(EXFLD_RATECARD);
			row.createCell(++columnCount).setCellValue(EXFLD_COMPETENCY);
			row.createCell(++columnCount).setCellValue(EXFLD_SKILLSET);
			row.createCell(++columnCount).setCellValue(EXFLD_CONFIDENCE);
			row.createCell(++columnCount).setCellValue(EXFLD_FTE_COUNT);
			row.createCell(++columnCount).setCellValue(EXFLD_BILLABILITY);
			row.createCell(++columnCount).setCellValue(EXFLD_REQUIRED_BY_DATE);
			row.createCell(++columnCount).setCellValue(EXFLD_BILLING_START_DATE);
			row.createCell(++columnCount).setCellValue(EXFLD_BILLING_END_DATE);
			row.createCell(++columnCount).setCellValue(EXFLD_ASSOCIATE_ID);
			row.createCell(++columnCount).setCellValue(EXFLD_ASSOCIATE_NAME);
			row.createCell(++columnCount).setCellValue(EXFLD_RESOURCE_STATUS);
			row.createCell(++columnCount).setCellValue(EXFLD_REMARKS);

			for (int i = 0; i < CALFLD_REVENUE.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_REVENUE[i]);
			}

			for (int i = 0; i < CALFLD_REVENUE_WEIGHTED.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_REVENUE_WEIGHTED[i]);
			}

			for (int i = 0; i < CALFLD_BILLED_FTE.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_BILLED_FTE[i]);
			}

			for (int i = 0; i < CALFLD_BILLABLE_DAYS.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_BILLABLE_DAYS[i]);
			}
			
			for (int i = 0; i < CALFLD_DIRECT_COST.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_DIRECT_COST[i]);
			}
			
			for (int i = 0; i < CALFLD_INDIRECT_COST.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_INDIRECT_COST[i]);
			}
			
			for (int i = 0; i < CALFLD_CP_DOLLAR.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_CP_DOLLAR[i]);
			}
			
			for (int i = 0; i < CALFLD_CP_PERCENT.length; i++) {
				row.createCell(++columnCount).setCellValue(CALFLD_CP_PERCENT[i]);
			}
		}
	}

	public boolean writeReport(List<ProjectVO> projectList) throws Exception {
		int rowCount = -1;
		
		Workbook workbook = ExcelUtil.createWorkbook(false);
		Sheet calcSheet = ExcelUtil.createCalculatedSheet(workbook);

		try {
			writeHeaderRow(calcSheet);
			++rowCount;
			
			for (ProjectVO revItem : projectList) {
				rowCount = writeEachRow(revItem, rowCount, workbook, calcSheet);
				logger.debug("Have written Record {} in the Report Excel.", rowCount);
			}

			if (calcSheet.getLastRowNum() != rowCount) {
				// This means there are some old records that we need to clean it up
				for (int i = rowCount + 1; i <= calcSheet.getLastRowNum(); i++) {
					calcSheet.removeRow(calcSheet.getRow(i));
				}
			}
			
			ExcelUtil.writeAndClose(workbook);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}

		return true;
	}

	public int writeEachRow(ProjectVO projectItem, int rowCount, Workbook workbook, Sheet calcSheet) throws Exception {

		ArrayList<ResourceVO> listResources = projectItem.getResources();
		for (int i = 0; i < listResources.size(); i++) {
			int columnCount = -1;
			Row row = calcSheet.createRow(++rowCount);
			ResourceVO resourceItem = listResources.get(i);
			CalculatedNumbersVO calcVO = resourceItem.getCalculatedFieldsVO();
			
			createCellString(row, ++columnCount, workbook, resourceItem.getRscErrVO().toString(), "");
			createCellString(row, ++columnCount, workbook, StaticCache.getDEMAssociateName(projectItem.getDemOffshoreId()), "");
			createCellString(row, ++columnCount, workbook, StaticCache.getDEMAssociateName(projectItem.getDemOnsiteId()), "");
			createCellInteger(row, ++columnCount, workbook, resourceItem.getId(), resourceItem.getRscErrVO().getIdError());
			createCellString(row, ++columnCount, workbook, resourceItem.getParentCustomerName(), resourceItem.getRscErrVO().getParentCustomerNameError());
			createCellString(row, ++columnCount, workbook, resourceItem.getOpptyProjectName(), resourceItem.getRscErrVO().getOpptyProjectNameError());

			createCellInteger(row, ++columnCount, workbook, projectItem.getESAProjectId(), projectItem.getPrjErrVO().getESAProjectIdError());
			createCellString(row, ++columnCount, workbook, projectItem.getProjectTypeValue(), projectItem.getPrjErrVO().getProjectTypeError());
			createCellString(row, ++columnCount, workbook, projectItem.getStatusValue(), projectItem.getPrjErrVO().getStatusValueError());
			createCellString(row, ++columnCount, workbook, projectItem.getRemarks(), projectItem.getPrjErrVO().getRemarksError());
			
			createCellInteger(row, ++columnCount, workbook, resourceItem.getSOUniqueNumber(), resourceItem.getRscErrVO().getSOUniqueNumberError());
			createCellString(row, ++columnCount, workbook, resourceItem.getGrade(), resourceItem.getRscErrVO().getGradeError());
			createCellString(row, ++columnCount, workbook, resourceItem.getLocation(), resourceItem.getRscErrVO().getLocationError());
			createCellCurrency(row, ++columnCount, workbook, resourceItem.getRateCard(), resourceItem.getRscErrVO().getRateCardError());
			createCellString(row, ++columnCount, workbook, resourceItem.getCompetency(), resourceItem.getRscErrVO().getCompetencyError());
			createCellString(row, ++columnCount, workbook, resourceItem.getSkillset(), resourceItem.getRscErrVO().getSkillsetError());
			createCellPercent(row, ++columnCount, workbook, resourceItem.getConfidence(), resourceItem.getRscErrVO().getConfidenceError());
			createCellDecimal(row, ++columnCount, workbook, resourceItem.getFTECount(), resourceItem.getRscErrVO().getFTECountError());
			createCellString(row, ++columnCount, workbook, resourceItem.getBillability(), resourceItem.getRscErrVO().getBillabilityError());
			createCellDate(row, ++columnCount, workbook, resourceItem.getRequiredByDate(), resourceItem.getRscErrVO().getRequiredByDateError());
			createCellDate(row, ++columnCount, workbook, resourceItem.getBillingStartDate(), resourceItem.getRscErrVO().getBillingStartDateError());
			createCellDate(row, ++columnCount, workbook, resourceItem.getBillingEndDate(), resourceItem.getRscErrVO().getBillingEndDateError());
			createCellInteger(row, ++columnCount, workbook, resourceItem.getAssociateId(), resourceItem.getRscErrVO().getAssociateIdError());
			createCellString(row, ++columnCount, workbook, resourceItem.getAssociateName(), resourceItem.getRscErrVO().getAssociateNameError());
			createCellString(row, ++columnCount, workbook, resourceItem.getResourceStatus(), resourceItem.getRscErrVO().getResourceStatusError());
			createCellString(row, ++columnCount, workbook, resourceItem.getRemarks(), resourceItem.getRscErrVO().getRemarksError());
			
			//write all revenue data
			for(int month = 1; month<=17; month++){
				createCellCurrency(row, ++columnCount, workbook, calcVO.getRevenue(month), "");
			}

			//write all weighted revenue data
			for(int month = 1; month<=17; month++){
				createCellCurrency(row, ++columnCount, workbook, calcVO.getWeightedRevenue(month), "");
			}

			//write all billed fte data
			for(int month = 1; month<=17; month++){
				createCellDecimal(row, ++columnCount, workbook, calcVO.getBilledFte(month), "");
			}

			//write all billed days data
			for(int month = 1; month<=17; month++){
				createCellInteger(row, ++columnCount, workbook, calcVO.getBilledDays(month), "");
			}
			
			//write all direct cost data
			for(int month = 1; month<=17; month++){
				createCellCurrency(row, ++columnCount, workbook, calcVO.getDirectCost(month), "");
			}
			
			//write all indirect cost data
			for(int month = 1; month<=17; month++){
				createCellCurrency(row, ++columnCount, workbook, calcVO.getIndirectCost(month), "");
			}
			
			//write all cp dollar data
			for(int month = 1; month<=17; month++){
				createCellCurrency(row, ++columnCount, workbook, calcVO.getCPDollar(month), "");
			}
			
			//write all cp percent data
			for(int month = 1; month<=17; month++){
				createCellPercent(row, ++columnCount, workbook, calcVO.getCPPercent(month), "");
			}

		}
		return rowCount;
	}

}
