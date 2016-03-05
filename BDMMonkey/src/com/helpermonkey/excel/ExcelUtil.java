package com.helpermonkey.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticMasterData;

public class ExcelUtil extends AbstractExcelHandler {

	private static final Logger logger = LoggerFactory.getLogger(SettingsExcelHandler.class);
	
	public static boolean isFileImpl = false;
	
	public static int DATE_STYLE_INDEX = 0;
	public static int PERCENT_STYLE_INDEX = 1;
	public static int INT_STYLE_INDEX = 2;
	public static int CURRENCY_STYLE_INDEX = 3;
	public static int DECIMAL_STYLE_INDEX = 4;
	
	public static int ERR_DATE_STYLE_INDEX = 5;
	public static int ERR_PERCENT_STYLE_INDEX = 6;
	public static int ERR_INT_STYLE_INDEX = 7;
	public static int ERR_CURRENCY_STYLE_INDEX = 8;
	public static int ERR_DECIMAL_STYLE_INDEX = 9;
	public static int ERR_STRING_STYLE_INDEX = 10;
	
	public ExcelUtil() throws Exception {
		// workbook = new XSSFWorkbook(new FileInputStream(StaticMasterData.RnR_CURRENT_FILE));

		ZipSecureFile.setMinInflateRatio(0.001);
//		File currentFile = new File(StaticMasterData.RnR_CURRENT_FILE);
//		Workbook workbook = WorkbookFactory.create(currentFile);

	}
	
	public static Workbook createWorkbook(boolean forRead) throws Exception {
		ZipSecureFile.setMinInflateRatio(0.001);
		XSSFWorkbook workbook;
		
		if(isFileImpl){
			workbook = (XSSFWorkbook) WorkbookFactory.create(new File(StaticMasterData.CURRENT_FILE));
		}else{
			workbook = new XSSFWorkbook(new FileInputStream(StaticMasterData.CURRENT_FILE));
		}

		//SXSSFWorkbook workbook = new SXSSFWorkbook(xworkbook, 250);
		
		CreationHelper createHelper = workbook.getCreationHelper();
		StylesTable styleTable = workbook.getStylesSource();
		
		//index 0
		CellStyle dateStyle = workbook.createCellStyle();
		dateStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.DATE_FORMAT));
		DATE_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)dateStyle);
		
		//index 1
		CellStyle percentStyle = workbook.createCellStyle();
		percentStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.PERCENT_STYLE));
		PERCENT_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)percentStyle);

		//index 2
		CellStyle numericStyle = workbook.createCellStyle();
		numericStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.INT_FORMAT));
		INT_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)numericStyle);
		
		//index 3
		CellStyle currencyStyle = workbook.createCellStyle();
		currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.CURRENCY_STYLE));
		CURRENCY_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)currencyStyle);
		
		//index 4
		CellStyle decimalStyle = workbook.createCellStyle();
		decimalStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.DECIMAL_FORMAT));
		DECIMAL_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)decimalStyle);

		/* *** same styles but for error highlighting the cell in red to indicate which cell has caused the error *** */
		Font redFont = workbook.createFont();
		redFont.setColor(HSSFColor.RED.index);

		
		//index 5
		CellStyle dateStyleErr = workbook.createCellStyle();
		dateStyleErr.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.DATE_FORMAT));
		dateStyleErr.setFont(redFont);
		ERR_DATE_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)dateStyleErr);
		
		//index 6
		CellStyle percentStyleErr = workbook.createCellStyle();
		percentStyleErr.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.PERCENT_STYLE));
		percentStyleErr.setFont(redFont);
		ERR_PERCENT_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)percentStyleErr);

		//index 7
		CellStyle numericStyleErr = workbook.createCellStyle();
		numericStyleErr.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.INT_FORMAT));
		numericStyleErr.setFont(redFont);
		ERR_INT_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)numericStyleErr);
		
		//index 8
		CellStyle currencyStyleErr = workbook.createCellStyle();
		currencyStyleErr.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.CURRENCY_STYLE));
		currencyStyleErr.setFont(redFont);
		ERR_CURRENCY_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)currencyStyleErr);
		
		//index 9
		CellStyle decimalStyleErr = workbook.createCellStyle();
		decimalStyleErr.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.DECIMAL_FORMAT));
		decimalStyleErr.setFont(redFont);
		ERR_DECIMAL_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)decimalStyleErr);
		
		//index 10
		CellStyle stringStyleErr = workbook.createCellStyle();
		stringStyleErr.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.DECIMAL_FORMAT));
		stringStyleErr.setFont(redFont);
		ERR_STRING_STYLE_INDEX = styleTable.putStyle((XSSFCellStyle)stringStyleErr);
		
		return workbook;
	}
	
	public static Sheet createProjectSheet(Workbook workbook) throws Exception {
		Sheet revenueSheet = workbook.getSheet(StaticMasterData.REVENUE_SHEET_NAME);
		if(revenueSheet == null){
			revenueSheet = workbook.createSheet(StaticMasterData.REVENUE_SHEET_NAME);
		}
		return revenueSheet;
	}
	
	public static Sheet createResourceSheet(Workbook workbook) throws Exception {
		Sheet rscsSheet = workbook.getSheet(StaticMasterData.RESOURCE_SHEET_NAME);
		if(rscsSheet == null){
			rscsSheet = workbook.createSheet(StaticMasterData.RESOURCE_SHEET_NAME);
		}
		return rscsSheet;
	}
	
	public static Sheet createCalculatedSheet(Workbook workbook) throws Exception {
		Sheet calcSheet = workbook.getSheet(StaticMasterData.CALCULATED_DATA_SHEET_NAME);
		if(calcSheet == null){
			calcSheet = workbook.createSheet(StaticMasterData.CALCULATED_DATA_SHEET_NAME);
		}
		return calcSheet;
	}
	
	public static CellStyle percentStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(PERCENT_STYLE_INDEX);
	}
	
	public static CellStyle dateStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(DATE_STYLE_INDEX);
	}
	
	public static CellStyle intStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(INT_STYLE_INDEX);
	}
	
	public static CellStyle decimalStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(DECIMAL_STYLE_INDEX);
	}

	public static CellStyle currencyStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(CURRENCY_STYLE_INDEX);
	}
	
	public static CellStyle errPercentStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(ERR_PERCENT_STYLE_INDEX);
	}
	
	public static CellStyle errDateStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(ERR_DATE_STYLE_INDEX);
	}
	
	public static CellStyle errIntStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(ERR_INT_STYLE_INDEX);
	}
	
	public static CellStyle errDecimalStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(ERR_DECIMAL_STYLE_INDEX);
	}

	public static CellStyle errCurrencyStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(ERR_CURRENCY_STYLE_INDEX);
	}
	
	public static CellStyle errStringStyle(Workbook workbook) throws Exception {
		StylesTable styleTable = ((XSSFWorkbook) workbook).getStylesSource();
		return styleTable.getStyleAt(ERR_STRING_STYLE_INDEX);
	}
	
	public static void writeAndClose(Workbook workbook) throws Exception {
		if(isFileImpl){
			writeAndCloseFile(workbook);
		}else{
			writeAndCloseStream(workbook);
		}
	}

	
	public static void writeAndCloseFile(Workbook workbook) throws Exception {
		File newFile = null;
		File currentFile = new File(StaticMasterData.CURRENT_FILE);
		
		try {
			double random = Math.random();
			String newFileName = currentFile.getParent() + "/" + (Math.round(random * 1000)) + currentFile.getName();
			System.out.println("path of new file:" + newFileName);
			newFile = new File(newFileName);
			newFile.createNewFile();

			FileOutputStream outStream = new FileOutputStream(newFile);
			workbook.write(outStream);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
			workbook.close();
		}
		
		String currentPath = currentFile.getPath();
		currentFile.delete();

		newFile.renameTo(new File(currentPath));
	}

	public static void writeAndCloseStream(Workbook workbook) throws Exception {
		try {
			FileOutputStream outStream = new FileOutputStream(StaticMasterData.CURRENT_FILE);
			workbook.write(outStream);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally{
			workbook.close();
		}
	}


	// XSSFCellStyle style = workbook.createCellStyle();
	// style.setAlignment(CellStyle.ALIGN_RIGHT);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);


}
