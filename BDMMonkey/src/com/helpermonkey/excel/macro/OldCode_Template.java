package com.helpermonkey.excel.macro;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class OldCode_Template {

//	private static final Logger logger = LoggerFactory.getLogger(CalculatedNumbersTransformer.class);

	public OldCode_Template() throws Exception {
	}
	
	/***   CalculatedNumbersTransformer related code ***/

//	public List<CalculatedNumbersVO> toCalculatedVOList(ClientEntitySetIterator<ClientEntitySet, ClientEntity> iterator)
//			throws Exception {
//		List<CalculatedNumbersVO> calcList = new ArrayList<CalculatedNumbersVO>();
//
//		int recordCount = 0;
//		while (iterator.hasNext()) {
//			ClientEntity calcEntity = iterator.next();
//
//			// first transform the client data pulled form SharePoint into
//			// Resource ItemVO
//			CalculatedNumbersVO calcVO = toCalculatedNumbersVO(calcEntity, ++recordCount);
//
//			calcList.add(calcVO);
//			logger.debug("Record Number {}. Transformed Calculated Data ID {}.", recordCount, calcVO.getId());
//		}
//		return calcList;
//	}
//
//	public CalculatedNumbersVO toCalculatedNumbersVO(ClientEntity entity, int rowNumber) throws Exception {
//		CalculatedNumbersVO calcVO = new CalculatedNumbersVO();
//
//		calcVO.setRowNumber(rowNumber);
//
//		List<ClientProperty> properties = entity.getProperties();
//
//		for (ClientProperty property : properties) {
//			// Set the resource item id which is the FK for this VO and also the
//			// confidence value for wghtd numbers calculation
//			if (FLD_ID.equalsIgnoreCase(property.getName())) {
//				calcVO.setId(getIntValue(property, 0));
//			}
//			if ("ResourceId".equalsIgnoreCase(property.getName())) {
//				calcVO.setResourceId(getIntValue(property, 0));
//			}
//			if ("Confidence".equalsIgnoreCase(property.getName())) {
//				calcVO.setConfidence(getDoubleValue(property, MonkeyConstants.DEFAULT_CONFIDENCE));
//			}
//			if ("JanRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setJanRev(getDoubleValue(property, 0));
//			}
//			if ("FebRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setFebRev(getDoubleValue(property, 0));
//			}
//			if ("MarRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setMarRev(getDoubleValue(property, 0));
//			}
//			if ("AprRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setAprRev(getDoubleValue(property, 0));
//			}
//			if ("MayRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setMayRev(getDoubleValue(property, 0));
//			}
//			if ("JunRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setJunRev(getDoubleValue(property, 0));
//			}
//			if ("JulRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setJulRev(getDoubleValue(property, 0));
//			}
//			if ("AugRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setAugRev(getDoubleValue(property, 0));
//			}
//			if ("SepRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setSepRev(getDoubleValue(property, 0));
//			}
//			if ("OctRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setOctRev(getDoubleValue(property, 0));
//			}
//			if ("NovRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setNovRev(getDoubleValue(property, 0));
//			}
//			if ("DecRev".equalsIgnoreCase(property.getName())) {
//				calcVO.setDecRev(getDoubleValue(property, 0));
//			}
//		}
//
//		// calcVO = calculateTheNumbers(calcVO);
//		return calcVO;
//
//	}


//	public ClientEntity calculateRevenueAndCreateEntity(ResourceItemVO resourceVO, CalculatedNumbersVO calcVO)
//			throws Exception {
//
//		calcVO = calculateTheNumbers(resourceVO, calcVO);
//
//		ClientEntity calculatedEntity = objFactory.newEntity(new FullQualifiedName(FQNAME_CALCULATED_ITEM));
//
//		// if there is no id value in the excel row, it means this is a new
//		// record for insert
//		if (resourceVO.getId() == 0) {
//			// this is a insert situation. so don't do anything.
//		} else {
//			// update
//			calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("Id",
//					objFactory.newPrimitiveValueBuilder().buildInt32(calcVO.getId())));
//			calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("ResourceId",
//					objFactory.newPrimitiveValueBuilder().buildInt32(calcVO.getResourceId())));
//			calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("Confidence",
//					objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getConfidence())));
//		}
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("JanRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getJanRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("FebRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getFebRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("MarRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getMarRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("AprRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getAprRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("MayRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getMayRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("JunRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getJunRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("JulRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getJulRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("AugRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getAugRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("SepRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getSepRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("OctRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getOctRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("NovRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getNovRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("DecRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getDecRev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("Q1Rev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getQ1Rev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("Q2Rev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getQ2Rev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("Q3Rev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getQ3Rev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("Q4Rev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getQ4Rev())));
//
//		calculatedEntity.getProperties().add(objFactory.newPrimitiveProperty("FYRev",
//				objFactory.newPrimitiveValueBuilder().buildDouble(calcVO.getFYRev())));
//
//		return calculatedEntity;
//	}
	
	
	

	
	/***   ResourceExcelHandler related code ***/
	
	
	// public boolean writeNewCalculatedSheet(List<CalculatedNumbersVO>
	// calcLLst, boolean paginated) throws Exception {
	//
	// XSSFWorkbook workbook = new XSSFWorkbook(new
	// FileInputStream(StaticMasterData.RnR_CURRENT_FILE));
	// XSSFSheet calcSheet =
	// workbook.getSheet(StaticMasterData.CALCULATED_DATA_SHEET_NAME);
	//
	// int rowCount = -1;
	//
	// // if not paginated then we are writing the first set of data, hence
	// // Write the header names row (first row) before looping through the
	// // data
	// if (!paginated) {
	// resourceRowCount = -1;
	// writeCalculatedFieldHeaderRow();
	// ++rowCount;
	// } else {
	// // in case we are updating an excel we need to make sure we include
	// // the
	// // rows at the bottom of the excel
	// // this is applicable only if the data is paginated, if not
	// // paginated,
	// // we anyways pull down the entire data
	// // and refresh the sheet so the rowCount has to be from -1 only.
	// rowCount = resourceRowCount;
	// }
	//
	// for (CalculatedNumbersVO calcItem : calcLLst) {
	// resourceRowCount = rowCount;
	// writeCalculatedFieldsToExcel(calcItem, ++rowCount);
	// }
	//
	// if (calcSheet.getLastRowNum() != rowCount) {
	// // This means there are some old records that we need to clean it up
	// for (int i = rowCount + 1; i <= calcSheet.getLastRowNum(); i++) {
	// calcSheet.removeRow(calcSheet.getRow(i));
	// }
	// }
	//
	// FileOutputStream outStream = new
	// FileOutputStream(StaticMasterData.RnR_CURRENT_FILE);
	// workbook.write(outStream);
	//
	// return true;
	// }

	// public void writeCalculatedFieldsToExcel(CalculatedNumbersVO calcVO, int
	// rowCount) throws Exception {
	// Row row = calcSheet.createRow(rowCount);
	// Cell cell = null;
	// int columnCount = -1;
	//
	// XSSFCellStyle intStyle = workbook.createCellStyle();
	// intStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.INT_FORMAT));
	//
	// XSSFCellStyle percentStyle = workbook.createCellStyle();
	// percentStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.PERCENT_STYLE));
	//
	// XSSFCellStyle currencyStyle = workbook.createCellStyle();
	// currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.CURRENCY_STYLE));
	//
	// //
	// currencyStyle.setDataFormat(createHelper.createDataFormat().getFormat(MonkeyConstants.DATE_FORMAT));
	// // currencyStyle.setFillBackgroundColor(HSSFColor.RED.index);
	// // currencyStyle.setFillPattern(XSSFCellStyle.LESS_DOTS);
	// // currencyStyle.setAlignment(XSSFCellStyle.ALIGN_FILL);
	// // spreadsheet.setColumnWidth(1,8000);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getId());
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getResourceId());
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getConfidence());
	// cell.setCellStyle(percentStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getJanRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getFebRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getMarRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getAprRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getMayRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getJunRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getJulRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getAugRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getSepRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getOctRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getNovRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getDecRev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getQ1Rev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getQ2Rev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getQ3Rev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getQ4Rev());
	// cell.setCellStyle(currencyStyle);
	//
	// cell = row.createCell(++columnCount);
	// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
	// cell.setCellValue(calcVO.getFYRev());
	// cell.setCellStyle(currencyStyle);
	//
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getJanRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getFebRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getMarRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getAprRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getMayRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getJunRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getJulRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getAugRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getSepRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getOctRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getNovRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getDecRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getQ1RevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getQ2RevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getQ3RevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getQ4RevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// // cell = row.createCell(++columnCount);
	// // cell.setCellValue(calcVO.getFYRevWghtd());
	// // cell.setCellStyle(currencyStyle);
	// }

	// public void writeCalculatedFieldHeaderRow() throws Exception {
	// int columnCount = -1;
	//
	// boolean skipWritingHeader = false;
	// Iterator<Row> iterator = calcSheet.iterator();
	//
	// while (iterator.hasNext()) {
	// Row nextRow = iterator.next();
	// Iterator<Cell> cellIterator = nextRow.cellIterator();
	//
	// // consider first row as the header row and check if header is
	// // already present
	// while (cellIterator.hasNext()) {
	// Cell nextCell = cellIterator.next();
	// // if there is a value that already exists, then we should not
	// // write the header again
	// if (EXFLD_ID.equalsIgnoreCase(getStringValue(nextCell))) {
	// // skip writing the header.
	// skipWritingHeader = true;
	// break;
	// }
	// }
	// }
	//
	// if (!skipWritingHeader) {
	// Row row = calcSheet.createRow(0);
	// row.createCell(++columnCount).setCellValue(EXFLD_ID);
	// row.createCell(++columnCount).setCellValue(CALFLD_RESOURCE_ID);
	// row.createCell(++columnCount).setCellValue(EXFLD_CONFIDENCE);
	// for (int i = 0; i < 17; i++) {
	// row.createCell(++columnCount).setCellValue(CALFLD_REVENUE[i]);
	// }
	// }
	// }

	

	// need to validate designation and other selected/lookup values in a
	// different way
	// ParentCustomerVO pcustVO =
	// StaticCache.getParentCustomerByName(resourceItem.getParentCustomerName());
	// if (pcustVO != null) {
	// if (pcustVO.getRateCard(resourceItem.getProjectRole(),
	// resourceItem.getLocation()) == 0) {
	// resourceItem.setErrorDetails(
	// "Invalid ProjectRole, this ProjectRole is leading to a RateCard of
	// $0, please check rate card master for the customer & project role you
	// have selected.");
	// }
	// }

	// public HashMap<Integer, CalculatedNumbersVO> readCalculatedDataFromExcel()
	// throws Exception {
	//
	// HashMap<Integer, CalculatedNumbersVO> calcMap = new HashMap<Integer,
	// CalculatedNumbersVO>();
	//
	// Iterator<Row> iterator = calcSheet.iterator();
	//
	// boolean isFirstRow = true;
	// Map<Integer, String> headerNames = new HashMap<Integer, String>();
	//
	// while (iterator.hasNext()) {
	// Row nextRow = iterator.next();
	// Iterator<Cell> cellIterator = nextRow.cellIterator();
	// CalculatedNumbersVO calcVO = new CalculatedNumbersVO();
	//
	// // save the row number in excel into the VO for later usage
	// calcVO.setRowNumber(nextRow.getRowNum());
	// logger.debug("Reading Calculated Sheet, Row Number {}",
	// calcVO.getRowNumber());
	//
	// // consider first row as the header row, which has the name of the
	// // column
	// if (isFirstRow) {
	// headerNames = processHeaderRow(cellIterator);
	// isFirstRow = false;
	// } else {
	// while (cellIterator.hasNext()) {
	// Cell nextCell = cellIterator.next();
	// int columnIndex = nextCell.getColumnIndex();
	//
	// switch ((String) headerNames.get(new Integer(columnIndex))) {
	// case EXFLD_ID:
	// calcVO.setId(getIntValue(nextCell));
	// break;
	// case CALFLD_RESOURCE_ID:
	// calcVO.setResourceId(getIntValue(nextCell));
	// break;
	// case EXFLD_CONFIDENCE:
	// calcVO.setConfidence(getDoubleValue(nextCell));
	// break;
	// case "JanRev":
	// calcVO.setJanRev(getDoubleValue(nextCell));
	// break;
	// case "FebRev":
	// calcVO.setFebRev(getDoubleValue(nextCell));
	// break;
	// case "MarRev":
	// calcVO.setMarRev(getDoubleValue(nextCell));
	// break;
	// case "AprRev":
	// calcVO.setAprRev(getDoubleValue(nextCell));
	// break;
	// case "MayRev":
	// calcVO.setMayRev(getDoubleValue(nextCell));
	// break;
	// case "JunRev":
	// calcVO.setJunRev(getDoubleValue(nextCell));
	// break;
	// case "JulRev":
	// calcVO.setJulRev(getDoubleValue(nextCell));
	// break;
	// case "AugRev":
	// calcVO.setAugRev(getDoubleValue(nextCell));
	// break;
	// case "SepRev":
	// calcVO.setSepRev(getDoubleValue(nextCell));
	// break;
	// case "OctRev":
	// calcVO.setOctRev(getDoubleValue(nextCell));
	// break;
	// case "NovRev":
	// calcVO.setNovRev(getDoubleValue(nextCell));
	// break;
	// case "DecRev":
	// calcVO.setDecRev(getDoubleValue(nextCell));
	// break;
	// case "Q1Rev":
	// calcVO.calculateQtrly();
	// break;
	// }
	// }
	// calcMap.put(new Integer(calcVO.getResourceId()), calcVO);
	// }
	// }
	//
	// return calcMap;
	// }


	// public boolean updateResourceToExcel(ResourceItemVO resourceItem,
	// CalculatedNumbersVO calcVO) throws Exception {
	// XSSFWorkbook workbook = new XSSFWorkbook(new
	// FileInputStream(StaticMasterData.RnR_CURRENT_FILE));
	//
	// writeResourceToExcel(resourceItem, resourceItem.getRowNumber(), false,
	// workbook);
	// if (calcVO.getRowNumber() == 0) {
	// // this means that this is an insert situation and given resource
	// // and calculated data are of a
	// // 1 to 1 relationship, we are assuming the row number of resource
	// // item can be the same as
	// // that of the calculated data as well.
	// calcVO.setRowNumber(resourceItem.getRowNumber());
	// }
	// writeCalculatedFieldsToExcel(calcVO, calcVO.getRowNumber());
	//
	// FileOutputStream outStream = new
	// FileOutputStream(StaticMasterData.RnR_CURRENT_FILE);
	// workbook.write(outStream);
	//
	// return true;
	// }
	// public boolean writeResourceToExcel(ResourceItemVO resourceItem, int
	// rowCount, boolean isSubList) throws Exception {
	// Workbook workbook = ExcelUtil.createWorkbook();
	//
	// boolean errorRecord = writeResourceToExcel(resourceItem, rowCount,
	// isSubList, workbook);
	//
	//
	// return errorRecord;
	// }
	
}
