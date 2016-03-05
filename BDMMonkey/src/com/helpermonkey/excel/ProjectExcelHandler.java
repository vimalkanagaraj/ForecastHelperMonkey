package com.helpermonkey.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.util.StaticMasterData;
import com.helpermonkey.vo.ParentCustomerVO;
import com.helpermonkey.vo.ProjectErrorVO;
import com.helpermonkey.vo.ProjectVO;

public class ProjectExcelHandler extends AbstractExcelHandler {

	private static final Logger logger = LoggerFactory.getLogger(ProjectExcelHandler.class);

	static int revenueRowCount;

	public ProjectExcelHandler() throws Exception {
	}

	public void writeHeaderRow(Sheet revenueSheet) throws Exception {
		int columnCount = -1;

		boolean skipWritingHeader = false;
		Iterator<Row> iterator = revenueSheet.iterator();

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
			Row row = revenueSheet.createRow(0);
			row.createCell(++columnCount).setCellValue(EXFLD_ERROR_DETAILS);
			row.createCell(++columnCount).setCellValue(EXFLD_CHANGE_FLAG);
			row.createCell(++columnCount).setCellValue(EXFLD_ID);
			row.createCell(++columnCount).setCellValue(EXFLD_OPPTY_PROJECT_NAME);
			row.createCell(++columnCount).setCellValue(EXFLD_ESA_PROJECT_ID);
			row.createCell(++columnCount).setCellValue(EXFLD_PARENT_CUSTOMER_NAME);
			row.createCell(++columnCount).setCellValue(EXFLD_PROJECT_TYPE);
			row.createCell(++columnCount).setCellValue(EXFLD_STATUS);
			row.createCell(++columnCount).setCellValue(EXFLD_REMARKS);
			if (StaticMasterData.REPORTING) {
				row.createCell(++columnCount).setCellValue(MonkeyConstants.FLD_DEM_OFFSHORE);
				row.createCell(++columnCount).setCellValue(MonkeyConstants.FLD_DEM_ONSITE);
			}

		}
	}

	public boolean writeRevenueToExcel(ProjectVO revItem, int rowCount, Workbook workbook, Sheet revenueSheet)
			throws Exception {

		if (revItem.getRowNumber() > 0) {
			rowCount = revItem.getRowNumber();
		}

		Row row = revenueSheet.createRow(rowCount);
		int columnCount = -1;
		boolean errorRecord = false;

		if (revItem.getPrjErrVO().toString() != null && revItem.getPrjErrVO().toString().length() > 1) {
			errorRecord = true;
			revItem.setChangeFlag(MonkeyConstants.CHANGE_FLAG_ERROR);
		}

		createCellString(row, ++columnCount, workbook, revItem.getPrjErrVO().toString(), "");
		createCellString(row, ++columnCount, workbook, revItem.getChangeFlag(), "");

		createCellInteger(row, ++columnCount, workbook, revItem.getId(), revItem.getPrjErrVO().getIdError());
		createCellString(row, ++columnCount, workbook, revItem.getOpptyProjectName(),
				revItem.getPrjErrVO().getOpptyProjectNameError());
		createCellInteger(row, ++columnCount, workbook, revItem.getESAProjectId(),
				revItem.getPrjErrVO().getESAProjectIdError());
		createCellString(row, ++columnCount, workbook, revItem.getParentCustomerName(),
				revItem.getPrjErrVO().getParentCustomerNameError());
		createCellString(row, ++columnCount, workbook, revItem.getProjectTypeValue(),
				revItem.getPrjErrVO().getProjectTypeError());
		createCellString(row, ++columnCount, workbook, revItem.getStatusValue(),
				revItem.getPrjErrVO().getStatusValueError());
		createCellString(row, ++columnCount, workbook, revItem.getRemarks(), revItem.getPrjErrVO().getRemarksError());

		if (StaticMasterData.REPORTING) {
			createCellInteger(row, ++columnCount, workbook, revItem.getDemOffshoreId(), "");
			createCellInteger(row, ++columnCount, workbook, revItem.getDemOnsiteId(), "");
		}

		return errorRecord;
	}

	public boolean writeRevenueVOToExcel(List<ProjectVO> listRevenue, boolean paginated, boolean cleanUnwantedRows)
			throws Exception {
		int rowCount = -1;

		Workbook workbook = ExcelUtil.createWorkbook(false);
		Sheet revenueSheet = ExcelUtil.createProjectSheet(workbook);

		// if not paginated then we are writing the first set of data, hence
		// Write the header names row (first row) before looping through the
		// data
		if (!paginated) {
			revenueRowCount = -1;
			writeHeaderRow(revenueSheet);
			++rowCount;
		} else {
			// in case we are updating an excel we need to make sure we include
			// the
			// rows at the bottom of the excel
			// this is applicable only if the data is paginated, if not
			// paginated,
			// we anyways pull down the entire data
			// and refresh the sheet so the rowCount has to be from -1 only.
			rowCount = revenueRowCount;
		}

		ArrayList<ProjectVO> errorRecords = new ArrayList<ProjectVO>();
		for (ProjectVO revItem : listRevenue) {
			revenueRowCount = rowCount;
			logger.debug("Have written Revenue Record {}", rowCount);
			if (writeRevenueToExcel(revItem, ++rowCount, workbook, revenueSheet)) {
				// if return value from write method is true, then this record
				// is an error record, so skip/remove this record
				// from further processing
				errorRecords.add(revItem);
			}
		}
		listRevenue.removeAll(errorRecords);

		if (revenueSheet.getLastRowNum() != rowCount && cleanUnwantedRows) {
			// This means there are some old records that we need to clean it up
			for (int i = rowCount + 1; i <= revenueSheet.getLastRowNum(); i++) {
				revenueSheet.removeRow(revenueSheet.getRow(i));
			}
		}

		ExcelUtil.writeAndClose(workbook);

		return true;
	}

	public ArrayList<ProjectVO> readRevenueSheetFromExcel() throws Exception {
		ArrayList<ProjectVO> listRevenue = new ArrayList<>();
		boolean validationErrorExists = false;

		Workbook workbook = ExcelUtil.createWorkbook(true);
		Sheet revenueSheet = ExcelUtil.createProjectSheet(workbook);

		try {
			Iterator<Row> iterator = revenueSheet.iterator();

			boolean isFirstRow = true;
			Map<Integer, String> headerNames = new HashMap<Integer, String>();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				ProjectVO revItem = new ProjectVO();
				// save the row number in excel into the VO for later usage
				revItem.setRowNumber(nextRow.getRowNum());
				logger.debug("Row Number {}", revItem.getRowNumber());

				// consider first row as the header row, which has the name of
				// the
				// column
				if (isFirstRow) {
					headerNames = processHeaderRow(cellIterator);
					isFirstRow = false;
				} else {
					while (cellIterator.hasNext()) {
						Cell nextCell = cellIterator.next();
						int columnIndex = nextCell.getColumnIndex();
						
						Object headerName = headerNames.get(new Integer(columnIndex));
						if(headerName == null){
							break;
						}

						switch ((String) headerName) {
						case EXFLD_ERROR_DETAILS:
							revItem.setPrjErrVO(new ProjectErrorVO());
							break;
						case EXFLD_CHANGE_FLAG:
							revItem.setChangeFlag(getStringValue(nextCell));
							if (MonkeyConstants.CHANGE_FLAG_ERROR.equalsIgnoreCase(revItem.getChangeFlag())) {
								revItem.setChangeFlag("");
							}
							break;
						case EXFLD_ID:
							int id = 0;
							try {
								id = getIntValue(nextCell);
							} catch (RuntimeException runex) {
								revItem.getPrjErrVO().setIdError(
										"ID is a numeric & generated value from SP, entered value is not numeric. User is not supposed to enter value.");
								break;
							}

							revItem.setId(id);
							break;
						case EXFLD_OPPTY_PROJECT_NAME:

							revItem.setOpptyProjectName(getStringValue(nextCell));
							break;
						case EXFLD_ESA_PROJECT_ID:
							long esaId = 0;
							try {
								esaId = getLongValue(nextCell);
							} catch (RuntimeException runex) {
								revItem.getPrjErrVO().setESAProjectIdError(
										"ESA Project ID has to be numeric, please enter a valid 8 to 12 char numeric value.");
								break;
							}

							revItem.setESAProjectId(esaId);
							break;
						case EXFLD_PARENT_CUSTOMER_NAME:
							revItem.setParentCustomerName(getStringValue(nextCell));
							break;
						case EXFLD_PROJECT_TYPE:
							revItem.setProjectTypeValue(getStringValue(nextCell));
							break;
						case EXFLD_REMARKS:
							revItem.setRemarks(getStringValue(nextCell));
							break;
						case EXFLD_STATUS:
							revItem.setStatusValue(getStringValue(nextCell));
							break;
						}
					}

					ParentCustomerVO pcustVO = StaticCache.getParentCustomerByName(revItem.getParentCustomerName());
					if (pcustVO != null) {
						revItem.setDemOffshoreId(pcustVO.getDEMOffshoreId());
						revItem.setDemOnsiteId(pcustVO.getDEMOnsiteId());
					}

					validate(revItem);
					
					// This is the one which validate if there is any error message at all
					// and if so sets the change flag to error
					revItem.calculateChangeFlag();

					if (MonkeyConstants.CHANGE_FLAG_ERROR.equalsIgnoreCase(revItem.getChangeFlag())) {
						validationErrorExists = true;
					}
					listRevenue.add(revItem);
				}
			}

			// if there is any validation error, then write back to excel and
			// stop/quit the program.
			if (validationErrorExists) {
				writeRevenueVOToExcel(listRevenue, false, false);
				logger.error("Validation Error Occurred, please open excel and see details.");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return listRevenue;
	}

	public void validate(ProjectVO revItem) throws Exception {
		// DO ALL BASIC Validations first.
		// check for mandatory fields first
		if (revItem.getOpptyProjectName() == null || revItem.getOpptyProjectName().length() < 1) {
			revItem.getPrjErrVO().setOpptyProjectNameError(
					"OpptyProjectName cannot be null or an empty string, please enter a value.");
			return;
		}
		if (revItem.getParentCustomerName() == null || revItem.getParentCustomerName().length() < 1) {
			revItem.getPrjErrVO().setParentCustomerNameError(
					"ParentCustomer cannot be null or an empty string, please select a value.");
			return;
		}
		if (revItem.getProjectTypeValue() == null || revItem.getProjectTypeValue().length() < 1) {
			revItem.getPrjErrVO().setProjectTypeError("Project Type cannot be null, please select a value");
			return;
		}
		if (revItem.getStatusValue() == null || revItem.getStatusValue().length() < 1) {
			revItem.getPrjErrVO().setStatusValueError("Project Status cannot be null, please select a value");
			return;
		}
		
		//Field valid values validation...
		if (!StaticCache.isValidProjectStatus(revItem.getStatusValue())) {
			revItem.getPrjErrVO().setStatusValueError("Invalid Project Status...");
		}
		
		if (!StaticCache.isValidProjectType(revItem.getProjectTypeValue())) {
			revItem.getPrjErrVO().setProjectTypeError(
					"Invalid Project Type...");
		}
		
		if (!StaticCache.isValidParentCustomer(revItem.getParentCustomerName())) {
			revItem.getPrjErrVO().setParentCustomerNameError(
					"ParentCustomer is not a valid parent customer, please select a valid one.");
		}

		// Other field validations...
		if (revItem.getOpptyProjectName().length() > 50) {
			revItem.getPrjErrVO().setOpptyProjectNameError(
					"OpptyProjectName is > 50 chars (too big), use Remarks column for details. ");
		}
		
		long twelveChar = 999999999999L;
		if (revItem.getESAProjectId() != 0
				&& (revItem.getESAProjectId() > twelveChar || revItem.getESAProjectId() < 11111111)) {
			revItem.getPrjErrVO().setESAProjectIdError("ESAProjectId cannot be > 12 chars long OR < 8 chars");
		}
		
		if (revItem.getRemarks() != null && revItem.getRemarks().length() > 254) {
			revItem.getPrjErrVO().setRemarksError("Remarks cannot be greater than 254 characters long ");
		}

		if (!StaticCache.canDEMModifyRecord(revItem.getParentCustomerName())) {
			revItem.getPrjErrVO().setParentCustomerNameError(
					"The current logged in User cannot insert/update on a parent customer not assigned to them.");
		}
	}
}
