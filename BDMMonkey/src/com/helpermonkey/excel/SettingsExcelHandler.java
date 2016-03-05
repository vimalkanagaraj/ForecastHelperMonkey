package com.helpermonkey.excel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticMasterData;
import com.helpermonkey.vo.ParentCustomerVO;

public class SettingsExcelHandler extends AbstractExcelHandler {

	private static final Logger logger = LoggerFactory.getLogger(SettingsExcelHandler.class);

	public SettingsExcelHandler() throws Exception {
	}

	public HashMap<String, ParentCustomerVO> loadParentCustomerFromExcel(Workbook workbook) throws Exception {
		HashMap<String, ParentCustomerVO> parentCustomerMap = new HashMap<String, ParentCustomerVO>();

		Sheet parentCustomerSheet = workbook.getSheet(StaticMasterData.PARENT_CUSTOMER_SHEET_NAME);

		try {
			Iterator<Row> iterator = parentCustomerSheet.iterator();
			boolean isFirstRow = true;

			String customerName = "";
			ParentCustomerVO customerVO = null;
			Map<Integer, String> headerNames = new HashMap<Integer, String>();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();

				logger.debug("ParentCustomer Sheet: Row Number {}", nextRow.getRowNum());

				// consider first row as the header row, which has the name of
				// the
				// column
				if (isFirstRow) {
					headerNames = processHeaderRow(cellIterator);
					isFirstRow = false;
				} else {
					customerVO = new ParentCustomerVO();
					while (cellIterator.hasNext()) {
						Cell nextCell = cellIterator.next();
						int columnIndex = nextCell.getColumnIndex();

						// Switch case does not line null values, so need this
						// to
						// ensure if you get null values turn it into empty
						// String
						String columnName = headerNames.get(new Integer(columnIndex)) == null ? ""
								: (String) headerNames.get(new Integer(columnIndex));

						switch (columnName) {
						case "CustomerId":
							customerVO.setId(readIntValue(nextCell));
							break;
						case "ParentCustomerName":
							customerName = getStringValue(nextCell);
							customerVO.setParentCustomerName(customerName);
							break;
						case "BillableHours":
							customerVO.setBillableHours(readIntValue(nextCell));
							break;
						case "DEMOffshore":
							customerVO.setDEMOffshoreId(readIntValue(nextCell));
							break;
						case "DEMOnsite":
							customerVO.setDEMOnsiteId(readIntValue(nextCell));
							break;
						default:
							break;
						}
					}
					parentCustomerMap.put(customerName, customerVO);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		// set the map to static so it becomes master data in memory
		return parentCustomerMap;
	}

	public static final String EXFLD_ID = "Id";
	public static final String EXFLD_NAME = "Name";
	public static final String EXFLD_BILLABLE_HOURS = "BillableHours";
	public static final String EXFLD_DISCOUNT = "Discount";

	public void writeParentCustomerHeader(Sheet parentCustomerSheet) throws Exception {
		int columnCount = -1;
		boolean skipWritingHeader = false;
		Iterator<Row> iterator = parentCustomerSheet.iterator();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();

			Iterator<Cell> cellIterator = nextRow.cellIterator();

			// consider first row as the header row and check if header is
			// already present
			while (cellIterator.hasNext()) {
				Cell nextCell = cellIterator.next();
				// if there is a value that already exists, then we should not
				// write the header again
				if (EXFLD_ID.equalsIgnoreCase(getStringValue(nextCell))) {
					// skip writing the header.
					skipWritingHeader = true;
					break;
				}
			}
		}

		if (!skipWritingHeader) {
			Row row = parentCustomerSheet.createRow(0);
			row.createCell(++columnCount).setCellValue(EXFLD_ID);
			row.createCell(++columnCount).setCellValue(EXFLD_NAME);
			row.createCell(++columnCount).setCellValue(EXFLD_BILLABLE_HOURS);
			row.createCell(++columnCount).setCellValue(EXFLD_DISCOUNT);
		}
	}

	public boolean writeParentCustomerToExcel(Workbook workbook, List<ParentCustomerVO> listParenTCustomer,
			boolean cleanUnwantedRows) throws Exception {
		int rowCount = -1;

		Sheet parentCustomerSheet = workbook.getSheet(StaticMasterData.PARENT_CUSTOMER_SHEET_NAME);
		CreationHelper createHelper = workbook.getCreationHelper();

		try {
			writeParentCustomerHeader(parentCustomerSheet);
			++rowCount;

			for (ParentCustomerVO pcustVO : listParenTCustomer) {
				writeParentCustomerToExcel(workbook, pcustVO, ++rowCount, parentCustomerSheet, createHelper);
			}

			if (parentCustomerSheet.getLastRowNum() != rowCount && cleanUnwantedRows) {
				// This means there are some old records that we need to clean
				// it up
				for (int i = rowCount + 1; i <= parentCustomerSheet.getLastRowNum(); i++) {
					parentCustomerSheet.removeRow(parentCustomerSheet.getRow(i));
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return true;
	}

	public boolean writeParentCustomerToExcel(Workbook workbook, ParentCustomerVO pcustVO, int rowCount, Sheet parentCustomerSheet,
			CreationHelper createHelper) throws Exception {

		Row row = parentCustomerSheet.createRow(rowCount);
		int columnCount = -1;

		row.createCell(++columnCount).setCellValue(pcustVO.getId());
		row.createCell(++columnCount).setCellValue(pcustVO.getParentCustomerName());
		row.createCell(++columnCount).setCellValue(pcustVO.getBillableHours());

		CellStyle percentStyle = workbook.createCellStyle();
		percentStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.PERCENT_STYLE));

		Cell cell = row.createCell(++columnCount);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell.setCellStyle(percentStyle);
		cell.setCellValue(pcustVO.getDiscount());
		return true;
	}
	
	public HashMap<String, Integer> loadGradewiseCost(Workbook workbook) throws Exception {
		HashMap<String, Integer> gradewiseCostMap = new HashMap<String, Integer>();
		String locationGrade = "";
		int monthlyCost = 0;

		Sheet holidaysSheet = workbook.getSheet(StaticMasterData.COST_SHEET_NAME);

		Iterator<Row> iterator = holidaysSheet.iterator();
		boolean isFirstRow = true;

		Map<Integer, String> headerNames = new HashMap<Integer, String>();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			logger.debug("Cost Sheet: Row Number {}", nextRow.getRowNum());

			// consider first row as the header row, which has the name of the
			// column
			if (isFirstRow) {
				headerNames = processHeaderRow(cellIterator);
				isFirstRow = false;
			} else {
				locationGrade = "";
				monthlyCost = 0;

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();

					// Switch case does not line null values, so need this to
					// ensure if you get null values turn it into empty String
					String columnName = headerNames.get(new Integer(columnIndex)) == null ? ""
							: (String) headerNames.get(new Integer(columnIndex));

					switch (columnName) {
					case "LocationGrade":
						locationGrade = getStringValue(nextCell);
						break;
					case "MonthlyCost":
						monthlyCost = getIntValue(nextCell);
						break;
					}
				}
				gradewiseCostMap.put(locationGrade, monthlyCost);
			}
		}
		return gradewiseCostMap;
	}

	public HashMap<String, ArrayList<Integer>> loadHolidaysFromExcel(Workbook workbook) throws Exception {
		HashMap<String, ArrayList<Integer>> locationHolidayMap = new HashMap<String, ArrayList<Integer>>();
		ArrayList<Integer> holidays = null;
		String locationName = "";

		Sheet holidaysSheet = workbook.getSheet(StaticMasterData.HOLIDAYS_SHEET_NAME);

		Iterator<Row> iterator = holidaysSheet.iterator();
		boolean isFirstRow = true;

		Map<Integer, String> headerNames = new HashMap<Integer, String>();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			logger.debug("Holidays Sheet: Row Number {}", nextRow.getRowNum());

			// consider first row as the header row, which has the name of the
			// column
			if (isFirstRow) {
				headerNames = processHeaderRow(cellIterator);
				isFirstRow = false;
			} else {
				// list of holidays for each location
				holidays = new ArrayList<Integer>();
				locationName = "";

				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();

					// Switch case does not line null values, so need this to
					// ensure if you get null values turn it into empty String
					String columnName = headerNames.get(new Integer(columnIndex)) == null ? ""
							: (String) headerNames.get(new Integer(columnIndex));

					switch (columnName) {
					case "Location":
						locationName = getStringValue(nextCell);
						break;
					case "":
						break;
					default:
						String dateValue = getStringValue(nextCell);
						if (dateValue != null && dateValue.length() > 1) {
							holidays.add(new Integer(toLocalDate(getStringValue(nextCell)).getDayOfYear()));
						}
						break;
					}
				}
				locationHolidayMap.put(locationName, holidays);
			}
		}
		return locationHolidayMap;
	}

	public HashMap<Double, ArrayList<Integer>> loadVacationsFromExcel(Workbook workbook) throws Exception {
		HashMap<Double, ArrayList<Integer>> vacationsMap = new HashMap<Double, ArrayList<Integer>>();
		// list of holidays for each location
		ArrayList<Integer> vacations = new ArrayList<Integer>();

		Sheet vacationsSheet = workbook.getSheet(StaticMasterData.VACATIONS_SHEET_NAME);

		Iterator<Row> iterator = vacationsSheet.iterator();
		boolean isFirstRow = true;
		double associateId = 999999;
		String vacationStartDate = null;
		String vacationEndDate = null;

		Map<Integer, String> headerNames = new HashMap<Integer, String>();

		while (iterator.hasNext()) {
			Row nextRow = iterator.next();
			Iterator<Cell> cellIterator = nextRow.cellIterator();

			logger.debug("Vacations Sheet: Row Number {}", nextRow.getRowNum());

			// consider first row as the header row, which has the name of the
			// column
			if (isFirstRow) {
				headerNames = processHeaderRow(cellIterator);
				isFirstRow = false;
			} else {
				while (cellIterator.hasNext()) {
					Cell nextCell = cellIterator.next();
					int columnIndex = nextCell.getColumnIndex();

					// Switch case does not line null values, so need this to
					// ensure if you get null values turn it into empty String
					String columnName = headerNames.get(new Integer(columnIndex)) == null ? ""
							: (String) headerNames.get(new Integer(columnIndex));

					switch (columnName) {
					case "AssociateID":
						associateId = getDoubleValue(nextCell);
						break;
					case "StartDate":
						vacationStartDate = new SimpleDateFormat(MonkeyConstants.DATE_FORMAT)
								.format(DateUtil.getJavaDate(getDoubleValue(nextCell)));
						break;
					case "EndDate":
						vacationEndDate = new SimpleDateFormat(MonkeyConstants.DATE_FORMAT)
								.format(DateUtil.getJavaDate(getDoubleValue(nextCell)));
						break;
					default:
						break;
					}
				}

				if (vacationStartDate != null && vacationStartDate.length() > 1 && vacationEndDate != null
						&& vacationEndDate.length() > 1) {
					int vacStartDayOfYr = (new Integer(toLocalDate(vacationStartDate).getDayOfYear()));
					int vacEndDayOfYr = (new Integer(toLocalDate(vacationEndDate).getDayOfYear()));
					int currentDayOfYr = vacStartDayOfYr;
					while (currentDayOfYr <= vacEndDayOfYr) {
						vacations.add(currentDayOfYr);
						currentDayOfYr++;
					}
					vacationsMap.put(associateId, vacations);
					vacations = new ArrayList<Integer>();
					associateId = 0;
					vacationStartDate = "";
					vacationEndDate = "";
				}

			}
		}

		return vacationsMap;
	}

	public int readIntValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			String value = cell.getStringCellValue();
			if (value == null || value.length() < 1) {
				return 0;
			} else {
				return new Double(value).intValue();
			}
		case Cell.CELL_TYPE_NUMERIC:
			return new Double(cell.getNumericCellValue()).intValue();
		}
		return 0;
	}

	// public HashMap<String, Double> loadRateCard() throws Exception {
	// HashMap<String, Double> rateCardMap = new HashMap<String, Double>();
	//
	// Iterator<Row> iterator = rateCardSheet.iterator();
	// boolean isFirstRow = true;
	//
	// String customerName = "";
	// String projectRole = "";
	//
	// Map<Integer, String> headerNames = new HashMap<Integer, String>();
	//
	// while (iterator.hasNext()) {
	// Row nextRow = iterator.next();
	// Iterator<Cell> cellIterator = nextRow.cellIterator();
	//
	// logger.debug("RateCard Sheet: Row Number {}", nextRow.getRowNum());
	//
	// // consider first row as the header row, which has the name of the
	// // column
	// if (isFirstRow) {
	// headerNames = processHeaderRow(cellIterator);
	// isFirstRow = false;
	// } else {
	// double rate = 0;
	// while (cellIterator.hasNext()) {
	// Cell nextCell = cellIterator.next();
	// int columnIndex = nextCell.getColumnIndex();
	//
	// switch ((String) headerNames.get(new Integer(columnIndex))) {
	// case "ParentCustomer":
	// customerName = getStringValue(nextCell);
	// break;
	// case "Project Role":
	// projectRole = getStringValue(nextCell);
	// break;
	// case "Rate Card US$":
	// rate = getDoubleValue(nextCell);
	// break;
	// }
	// }
	// rateCardMap.put(customerName + "_" + projectRole, new Double(rate));
	// }
	// }
	//
	// return rateCardMap;
	// }

}
