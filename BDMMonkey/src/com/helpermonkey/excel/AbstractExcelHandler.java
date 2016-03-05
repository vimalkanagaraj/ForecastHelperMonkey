package com.helpermonkey.excel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.joda.time.LocalDate;

import com.helpermonkey.common.MonkeyConstants;

public class AbstractExcelHandler {
	
	/*** Project related fields ***/
	
	public static final String EXFLD_ERROR_DETAILS = "ErrorDetails";
	
	public static final String EXFLD_CHANGE_FLAG = "ChangeFlag";

	public static final String EXFLD_REMARKS = "Remarks";

	public static final String EXFLD_STATUS = "Status";

	public static final String EXFLD_PROJECT_TYPE = "ProjectType";

	public static final String EXFLD_PARENT_CUSTOMER_NAME = "ParentCustomerName";

	public static final String EXFLD_CUSTOMER_ID = "CustomerId";

	public static final String EXFLD_ESA_PROJECT_ID = "ESAProjectID";

	public static final String EXFLD_OPPTY_PROJECT_NAME = "OpptyProjectName";
	
	/** Resource Allocation related fields ***/

	public static final String EXFLD_RESOURCE_STATUS = "ResourceStatus";

	public static final String EXFLD_ASSOCIATE_NAME = "AssociateName";

	public static final String EXFLD_ASSOCIATE_ID = "AssociateID";

	public static final String EXFLD_SO_NUMBER = "SOUniqueNo";

	public static final String EXFLD_CONFIDENCE = "Confidence";

	public static final String EXFLD_BILLING_END_DATE = "BillingEndDate";

	public static final String EXFLD_BILLING_START_DATE = "BillingStartDate";

	public static final String EXFLD_REQUIRED_BY_DATE = "RequiredByDate";

	public static final String EXFLD_FTE_COUNT = "FTECount";

	public static final String EXFLD_BILLABILITY = "Billability";

	public static final String EXFLD_SKILLSET = "Skillset";

	public static final String EXFLD_COMPETENCY = "Competency";

	public static final String EXFLD_RATECARD = "RateCard";

	public static final String EXFLD_LOCATION = "Location";

	public static final String EXFLD_GRADE = "Grade";

	public static final String EXFLD_ID = "Id";

	public static final String EXFLD_RESOURCE_ALLOCATION_ID = "ResourceAllocationId";
	
	/** Calculated Numbers related fields ***/
	
	public static final String CALFLD_RESOURCE_ID = "ResourceId";
	
	
	public static final String[] CALFLD_REVENUE_WEIGHTED = new String[]{"JanRevWg", "FebRevWg", "MarRevWg", "AprRevWg","MayRevWg","JunRevWg",
			"JulRevWg","AugRevWg","SepRevWg","OctRevWg","NovRevWg","DecRevWg","Q1RevWg","Q2RevWg","Q3RevWg","Q4RevWg","FYRevWg",};

	public static final String[] CALFLD_REVENUE = new String[]{"JanRev", "FebRev", "MarRev", "AprRev","MayRev","JunRev",
			"JulRev","AugRev","SepRev","OctRev","NovRev","DecRev","Q1Rev","Q2Rev","Q3Rev","Q4Rev","FYRev",};

	public static final String[] CALFLD_BILLED_FTE = new String[]{"JanBFte", "FebBFte", "MarBFte", "AprBFte","MayBFte","JunBFte",
			"JulBFte","AugBFte","SepBFte","OctBFte","NovBFte","DecBFte","Q1BFte","Q2BFte","Q3BFte","Q4BFte","FYBFte",};

	public static final String[] CALFLD_BILLABLE_DAYS = new String[]{"JanBDays", "FebBDays", "MarBDays", "AprBDays","MayBDays","JunBDays",
			"JulBDays","AugBDays","SepBDays","OctBDays","NovBDays","DecBDays","Q1BDays","Q2BDays","Q3BDays","Q4BDays","FYBDays",};

	public static final String[] CALFLD_DIRECT_COST = new String[]{"JanDirectCost", "FebDirectCost", "MarDirectCost", "AprDirectCost","MayDirectCost","JunDirectCost",
			"JulDirectCost","AugDirectCost","SepDirectCost","OctDirectCost","NovDirectCost","DecDirectCost","Q1DirectCost","Q2DirectCost","Q3DirectCost","Q4DirectCost","FYDirectCost",};

	public static final String[] CALFLD_INDIRECT_COST = new String[]{"JanInDirectCost", "FebInDirectCost", "MarInDirectCost", "AprInDirectCost","MayInDirectCost","JunInDirectCost",
			"JulInDirectCost","AugInDirectCost","SepInDirectCost","OctInDirectCost","NovInDirectCost","DecInDirectCost","Q1InDirectCost","Q2InDirectCost","Q3InDirectCost","Q4InDirectCost","FYInDirectCost",};
	
	public static final String[] CALFLD_CP_DOLLAR = new String[]{"JanCP$", "FebCP$", "MarCP$", "AprCP$","MayCP$","JunCP$",
			"JulCP$","AugCP$","SepCP$","OctCP$","NovCP$","DecCP$","Q1CP$","Q2CP$","Q3CP$","Q4CP$","FYCP$",};

	public static final String[] CALFLD_CP_PERCENT = new String[]{"JanCP%", "FebCP%", "MarCP%", "AprCP%","MayCP%","JunCP%",
			"JulCP%","AugCP%","SepCP%","OctCP%","NovCP%","DecCP%","Q1CP%","Q2CP%","Q3CP%","Q4CP%","FYCP%",};

//	private static final Logger  logger = LoggerFactory.getLogger(AbstractExcelHandler.class);
	
	public HashMap<Integer, String> processHeaderRow(Iterator<Cell> cellIterator){
		HashMap<Integer, String> headerNames = new HashMap<Integer, String>();
		while (cellIterator.hasNext()) {
			Cell nextCell = cellIterator.next();
			int columnIndex = nextCell.getColumnIndex();
			headerNames.put(new Integer(columnIndex), getStringValue(nextCell));
		}
		return headerNames;
	}
	
	public LocalDate toLocalDate(String date) {
		String[] strBuf = StringUtils.split(date, "-");
		LocalDate localDate = new LocalDate(new Integer(strBuf[0]).intValue(), new Integer(strBuf[1]).intValue(),
				new Integer(strBuf[2]).intValue());
		return localDate;
	}


	public int getIntValue(Cell cell) {
		return (int) cell.getNumericCellValue();
	}
	
	public long getLongValue(Cell cell) {
		return (long) cell.getNumericCellValue();
	}

	public double getDoubleValue(Cell cell) {
		
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return new Double(cell.getStringCellValue()).doubleValue();
		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		default:
			return 0;
		}
	}

	public String getStringValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			return "" + cell.getNumericCellValue();
		}
		return cell.getStringCellValue();
	}

	public Object getCellValue(Cell cell) {
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			return cell.getStringCellValue();

		case Cell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();

		case Cell.CELL_TYPE_NUMERIC:
			return cell.getNumericCellValue();
		}

		return null;
	}
	
	protected Cell createCell(Row row, int columnCount, int cellType) {
		Cell cell = row.createCell(columnCount);
		cell.setCellType(cellType);
		return cell;
	}

	protected void createCellString(Row row, int columnCount, Workbook workbook, String value, String errorMsg) throws Exception{
		Cell cell = createCell(row, columnCount, Cell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		if(errorMsg != null && errorMsg.length() > 0){
			cell.setCellStyle(ExcelUtil.errStringStyle(workbook));
		}
	}

	protected void createCellInteger(Row row, int columnCount, Workbook workbook, double value, String errorMsg) throws Exception {
		Cell cell = createCell(row, columnCount, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		if(errorMsg == null || errorMsg.length() < 1){
			cell.setCellStyle(ExcelUtil.intStyle(workbook));
		}else{
			cell.setCellStyle(ExcelUtil.errIntStyle(workbook));
		}
	}
	
	protected void createCellDecimal(Row row, int columnCount, Workbook workbook, double value, String errorMsg) throws Exception {
		Cell cell = createCell(row, columnCount, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		if(errorMsg == null || errorMsg.length() < 1){
			cell.setCellStyle(ExcelUtil.decimalStyle(workbook));
		}else{
			cell.setCellStyle(ExcelUtil.errDecimalStyle(workbook));
		}
	}
	
	protected Cell createCellPercent(Row row, int columnCount, Workbook workbook, double value, String errorMsg) throws Exception {
		Cell cell = createCell(row, columnCount, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		if(errorMsg == null || errorMsg.length() < 1){
			cell.setCellStyle(ExcelUtil.percentStyle(workbook));
		}else{
			cell.setCellStyle(ExcelUtil.errPercentStyle(workbook));
		}
		return cell;
	}
	
	protected Cell createCellCurrency(Row row, int columnCount, Workbook workbook, double value, String errorMsg) throws Exception {
		Cell cell = createCell(row, columnCount, Cell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		if(errorMsg == null || errorMsg.length() < 1){
			cell.setCellStyle(ExcelUtil.currencyStyle(workbook));
		}else{
			cell.setCellStyle(ExcelUtil.errCurrencyStyle(workbook));
		}
		return cell;
	}

	protected void createCellDate(Row row, int columnCount, Workbook workbook, String date, String errorMsg) throws Exception {
		Cell cell = createCell(row, columnCount, Cell.CELL_TYPE_STRING);
		if (date != null && date.length() > 1){
			cell.setCellValue(DateUtil.getExcelDate(new SimpleDateFormat(MonkeyConstants.DATE_FORMAT).parse(date)));
		}
		
		if(errorMsg == null || errorMsg.length() < 1){
			cell.setCellStyle(ExcelUtil.dateStyle(workbook));
		}else{
			cell.setCellStyle(ExcelUtil.errDateStyle(workbook));
		}
	}
	
}
