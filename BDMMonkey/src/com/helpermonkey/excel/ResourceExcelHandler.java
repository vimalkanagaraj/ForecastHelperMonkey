package com.helpermonkey.excel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.vo.ParentCustomerVO;
import com.helpermonkey.vo.ProjectVO;
import com.helpermonkey.vo.ResourceVO;

public class ResourceExcelHandler extends AbstractExcelHandler {

	private static final Logger logger = LoggerFactory.getLogger(ResourceExcelHandler.class);

	static int resourceRowCount;

	static HashMap<Integer, Double> associateAllocation = new HashMap<Integer, Double>();

	CreationHelper createHelper = null;

	public ResourceExcelHandler() throws Exception {
	}

	public void writeHeaderRow(Sheet resourceSheet) throws Exception {
		int columnCount = -1;

		boolean skipWritingHeader = false;
		Iterator<Row> iterator = resourceSheet.iterator();

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
			Row row = resourceSheet.createRow(0);
			row.createCell(++columnCount).setCellValue(EXFLD_ERROR_DETAILS);
			row.createCell(++columnCount).setCellValue(EXFLD_CHANGE_FLAG);
			row.createCell(++columnCount).setCellValue(EXFLD_ID);
			row.createCell(++columnCount).setCellValue(EXFLD_PARENT_CUSTOMER_NAME);
			row.createCell(++columnCount).setCellValue(EXFLD_SO_NUMBER);
			row.createCell(++columnCount).setCellValue(EXFLD_OPPTY_PROJECT_NAME);
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
		}
	}

	public boolean writeResourceToExcel(ResourceVO resourceItem, int rowCount, boolean isSubList, Workbook workbook)
			throws Exception {
		Sheet resourceSheet = ExcelUtil.createResourceSheet(workbook);

		if (isSubList && resourceItem.getRowNumber() > 0) {
			rowCount = resourceItem.getRowNumber();
		}
		Row row = resourceSheet.createRow(rowCount);
		int columnCount = -1;
		boolean errorRecord = false;

		createCellString(row, ++columnCount, workbook, resourceItem.getRscErrVO().toString(), "");
		createCellString(row, ++columnCount, workbook, resourceItem.getChangeFlag(), "");

		createCellInteger(row, ++columnCount, workbook, resourceItem.getId(), resourceItem.getRscErrVO().getIdError());
		createCellString(row, ++columnCount, workbook, resourceItem.getParentCustomerName(),
				resourceItem.getRscErrVO().getParentCustomerNameError());
		createCellInteger(row, ++columnCount, workbook, resourceItem.getSOUniqueNumber(),
				resourceItem.getRscErrVO().getSOUniqueNumberError());
		createCellString(row, ++columnCount, workbook, resourceItem.getOpptyProjectName(),
				resourceItem.getRscErrVO().getOpptyProjectNameError());
		createCellString(row, ++columnCount, workbook, resourceItem.getGrade(),
				resourceItem.getRscErrVO().getGradeError());
		createCellString(row, ++columnCount, workbook, resourceItem.getLocation(),
				resourceItem.getRscErrVO().getLocationError());
		createCellCurrency(row, ++columnCount, workbook, resourceItem.getRateCard(),
				resourceItem.getRscErrVO().getRateCardError());
		createCellString(row, ++columnCount, workbook, resourceItem.getCompetency(),
				resourceItem.getRscErrVO().getCompetencyError());
		createCellString(row, ++columnCount, workbook, resourceItem.getSkillset(),
				resourceItem.getRscErrVO().getSkillsetError());
		createCellPercent(row, ++columnCount, workbook, resourceItem.getConfidence(),
				resourceItem.getRscErrVO().getConfidenceError());
		createCellDecimal(row, ++columnCount, workbook, resourceItem.getFTECount(),
				resourceItem.getRscErrVO().getFTECountError());
		createCellString(row, ++columnCount, workbook, resourceItem.getBillability(),
				resourceItem.getRscErrVO().getBillabilityError());
		createCellDate(row, ++columnCount, workbook, resourceItem.getRequiredByDate(),
				resourceItem.getRscErrVO().getRequiredByDateError());
		createCellDate(row, ++columnCount, workbook, resourceItem.getBillingStartDate(),
				resourceItem.getRscErrVO().getBillingStartDateError());
		createCellDate(row, ++columnCount, workbook, resourceItem.getBillingEndDate(),
				resourceItem.getRscErrVO().getBillingEndDateError());
		createCellInteger(row, ++columnCount, workbook, resourceItem.getAssociateId(),
				resourceItem.getRscErrVO().getAssociateIdError());
		createCellString(row, ++columnCount, workbook, resourceItem.getAssociateName(),
				resourceItem.getRscErrVO().getAssociateNameError());
		createCellString(row, ++columnCount, workbook, resourceItem.getResourceStatus(),
				resourceItem.getRscErrVO().getResourceStatusError());
		createCellString(row, ++columnCount, workbook, resourceItem.getRemarks(),
				resourceItem.getRscErrVO().getRemarksError());

		return errorRecord;
	}

	public List<ResourceVO> writeNewResourceSheet(List<ResourceVO> listResource, boolean paginated, boolean isSublist)
			throws Exception {

		int rowCount = -1;

		Workbook workbook = ExcelUtil.createWorkbook(false);
		Sheet resourceSheet = ExcelUtil.createResourceSheet(workbook);

		try {
			// if not paginated then we are writing the first set of data, hence
			// Write the header names row (first row) before looping through the
			// data
			if (!paginated) {
				resourceRowCount = -1;
				/*
				 * ** vimal for now we are commenting this and assuming tha
				 * table with the first row as header will be manually created
				 * from a template or something, because writing the header
				 * after a table is created in the excel is causing issue when
				 * opening the excel...
				 */
				writeHeaderRow(resourceSheet);
				++rowCount;
			} else {
				// in case we are updating an excel we need to make sure we
				// include
				// the
				// rows at the bottom of the excel
				// this is applicable only if the data is paginated, if not
				// paginated,
				// we anyways pull down the entire data
				// and refresh the sheet so the rowCount has to be from -1 only.
				rowCount = resourceRowCount;
			}

			ArrayList<ResourceVO> errorRecords = new ArrayList<ResourceVO>();
			for (ResourceVO resourceItem : listResource) {
				if (writeResourceToExcel(resourceItem, ++rowCount, isSublist, workbook)) {
					// if return value from write method is true, then this
					// record
					// is an error record, so skip/remove this record
					// from further processing
					errorRecords.add(resourceItem);
				}
				resourceRowCount = rowCount;
				// writeCalculatedFieldsToExcel(resourceItem.getCalculatedFieldsVO(),
				// rowCount);

			}
			listResource.removeAll(errorRecords);

			if (resourceSheet.getLastRowNum() != rowCount && !isSublist) {
				// This means there are some old records that we need to clean
				// it up
				for (int i = rowCount + 1; i <= resourceSheet.getLastRowNum(); i++) {
					resourceSheet.removeRow(resourceSheet.getRow(i));
				}
			}

			ExcelUtil.writeAndClose(workbook);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return listResource;
	}

	public String checkTypeSafety(Cell nextCell, int expectedCellType, ResourceVO resourceVO, String errorMsg) {
		return errorMsg;
	}

	public List<ResourceVO> readResourceSheetFromExcel() throws Exception {
		Workbook workbook = ExcelUtil.createWorkbook(true);
		Sheet resourceSheet = ExcelUtil.createResourceSheet(workbook);

		List<ResourceVO> listResource = null;
		try {
			listResource = new ArrayList<>();
			boolean validationErrorExists = false;

			Iterator<Row> iterator = resourceSheet.iterator();

			boolean isFirstRow = true;
			Map<Integer, String> headerNames = new HashMap<Integer, String>();

			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				Iterator<Cell> cellIterator = nextRow.cellIterator();
				ResourceVO resourceVO = new ResourceVO();
				// save the row number in excel into the VO for later usage
				resourceVO.setRowNumber(nextRow.getRowNum());
				logger.debug("reading resource sheet, Row Number {}", resourceVO.getRowNumber());

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
							break;
						case EXFLD_CHANGE_FLAG:
							if (MonkeyConstants.CHANGE_FLAG_UPDATE.equalsIgnoreCase(getStringValue(nextCell))) {
								resourceVO.setChangeFlag(getStringValue(nextCell));
							}
							break;
						case EXFLD_ID:
							int id = 0;
							try {
								id = getIntValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setIdError(
										"ID is a numeric & generated value from SP, entered value is not numeric. User is not supposed to enter value.");
								break;
							}

							resourceVO.setId(id);
							break;
						case EXFLD_OPPTY_PROJECT_NAME:
							resourceVO.setOpptyProjectName(getStringValue(nextCell));
							break;
						case EXFLD_PARENT_CUSTOMER_NAME:
							resourceVO.setParentCustomerName(getStringValue(nextCell));
							break;
						case EXFLD_GRADE:
							resourceVO.setGrade(getStringValue(nextCell));
							break;
						case EXFLD_LOCATION:
							resourceVO.setLocation(getStringValue(nextCell));
							break;
						case EXFLD_RATECARD:
							double rateCard = 0;
							try {
								rateCard = getDoubleValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setRateCardError(
										"Rate Card is a numeric field, please enter a valid numeric value.");
								break;
							}

							resourceVO.setRateCard(rateCard);
							break;
						case EXFLD_COMPETENCY:
							resourceVO.setCompetency(getStringValue(nextCell));
							break;
						case EXFLD_SKILLSET:
							resourceVO.setSkillset(getStringValue(nextCell));
							break;
						case EXFLD_FTE_COUNT:
							double fteCount = 0;
							try {
								fteCount = getDoubleValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setFTECountError(
										"FTE Count is a numeric field, please enter a valid numeric value.");
								break;
							}

							resourceVO.setFTECount(fteCount);
							break;
						case EXFLD_BILLABILITY:
							resourceVO.setBillability(getStringValue(nextCell));
							break;
						case EXFLD_REQUIRED_BY_DATE:
							double excelDate = 0;
							try {
								excelDate = getDoubleValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setRequiredByDateError(
										"Required By Date is wrong, please enter a valid date.");
								break;
							}

							Date requiredByDate = DateUtil.getJavaDate(excelDate);
							if (requiredByDate == null) {
								resourceVO.getRscErrVO().setRequiredByDateError(
										"Required By Date is wrong, please enter a valid date.");
							} else {
								resourceVO.setRequiredByDate(
										new SimpleDateFormat(MonkeyConstants.DATE_FORMAT).format(requiredByDate));
							}
							break;
						case EXFLD_BILLING_START_DATE:
							excelDate = 0;
							try {
								excelDate = getDoubleValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setBillingStartDateError(
										"Billing Start Date is wrong, please enter a valid date.");
								break;
							}

							Date startDate = DateUtil.getJavaDate(excelDate);
							if (startDate == null) {
								resourceVO.getRscErrVO().setBillingStartDateError(
										"Billing Start Date is wrong, please enter a valid date.");
							} else {
								resourceVO.setBillingStartDate(
										new SimpleDateFormat(MonkeyConstants.DATE_FORMAT).format(startDate));
							}
							break;
						case EXFLD_BILLING_END_DATE:
							excelDate = 0;
							try {
								excelDate = getDoubleValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setBillingEndDateError(
										"Billing End Date is wrong, please enter a valid date.");
								break;
							}

							Date endDate = DateUtil.getJavaDate(excelDate);
							if (endDate == null) {
								resourceVO.getRscErrVO().setBillingEndDateError(
										"Billing End Date is wrong, please enter a valid date.");
							} else {
								resourceVO.setBillingEndDate(
										new SimpleDateFormat(MonkeyConstants.DATE_FORMAT).format(endDate));
							}
							break;
						case EXFLD_CONFIDENCE:
							double confidence = 0;
							try {
								confidence = getDoubleValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setConfidenceError(
										"Confidence is a numeric field, please enter a valid numeric value.");
								break;
							}

							resourceVO.setConfidence(confidence);
							break;
						case EXFLD_SO_NUMBER:
							int soNumber = 0;
							try {
								soNumber = getIntValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setSOUniqueNumberError(
										"SO Unique Number is a numeric field, please enter a valid numeric value.");
								break;
							}

							resourceVO.setSOUniqueNumber(soNumber);
							break;
						case EXFLD_ASSOCIATE_ID:
							int associateId = 0;
							try {
								associateId = getIntValue(nextCell);
							} catch (RuntimeException runex) {
								resourceVO.getRscErrVO().setAssociateIdError(
										"Associate Id is a numeric field, please enter a valid numeric value.");
								break;
							}

							resourceVO.setAssociateId(associateId);
							break;
						case EXFLD_ASSOCIATE_NAME:
							resourceVO.setAssociateName(getStringValue(nextCell));
							break;
						case EXFLD_RESOURCE_STATUS:
							resourceVO.setResourceStatus(getStringValue(nextCell));
							break;
						case EXFLD_REMARKS:
							resourceVO.setRemarks(getStringValue(nextCell));
							break;
						}
					}

					ParentCustomerVO pcustVO = StaticCache.getParentCustomerByName(resourceVO.getParentCustomerName());
					if (pcustVO != null) {
						resourceVO.setDemOffshoreId(pcustVO.getDEMOffshoreId());
						resourceVO.setDemOnsiteId(pcustVO.getDEMOnsiteId());
					}

					// validate the resource record that has been read from the
					// excel sheet.
					validate(resourceVO);

					// using the name of the project from the resource sheet,
					// select the revenue id from the static revenue list
					// this is much more reliable than reading from excel.
					ProjectVO revVO = StaticCache.getRevenueItem(resourceVO.getOpptyProjectName());
					if (revVO == null) {
						// something seriously wrong, there is a resource record
						// in excel for which there is no revenue record
						// This is serious FK constrint issue
						resourceVO.getRscErrVO().setOpptyProjectNameError(
								"project does not exist. Resource cannot be created without its project.");
						resourceVO.setParentCustomerName(null);
					} else {
						if (resourceVO.getParentCustomerName() == null
								|| resourceVO.getParentCustomerName().length() < 1) {
							// if the resource parent customer is null, then set
							// that with the parent customer
							// from revenue.
							resourceVO.setParentCustomerName(revVO.getParentCustomerName());
						}
					}
					
					// This is the one which validate if there is any error message at all
					// and if so sets the change flag to error
					resourceVO.calculateChangeFlag();

					listResource.add(resourceVO);
				}

				if (MonkeyConstants.CHANGE_FLAG_ERROR.equalsIgnoreCase(resourceVO.getChangeFlag())) {
					validationErrorExists = true;
				}
			}

			// if there is any validation error, then write back to excel and
			// stop/quit the program.
			if (validationErrorExists) {
				listResource = writeNewResourceSheet(listResource, false, false);
				// closeExcel();
				logger.error(
						"Validation Error Occurred in Resource Sheet, please open excel and see the error details.");
				// System.exit(0);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}

		return listResource;
	}

	public void validate(ResourceVO resourceItem) throws Exception {
		// DO ALL BASIC Validations first.
		// check for mandatory fields first
		if (resourceItem.getOpptyProjectName() == null || resourceItem.getOpptyProjectName().length() < 1) {
			resourceItem.getRscErrVO()
					.setOpptyProjectNameError("OpptyProjectName cannot be null, please enter a value.");
			return;
		}

		if (resourceItem.getParentCustomerName() == null || resourceItem.getParentCustomerName().length() < 1) {
			resourceItem.getRscErrVO()
					.setParentCustomerNameError("ParentCustomer cannot be null, please select a value.");
			return;
		}

		if (resourceItem.getGrade() == null || resourceItem.getGrade().length() < 1) {
			resourceItem.getRscErrVO().setGradeError("Grade cannot be null, please select a value");
			return;
		}

		if (resourceItem.getLocation() == null || resourceItem.getLocation().length() < 1) {
			resourceItem.getRscErrVO().setLocationError("Location cannot be null, please select a value");
			return;
		}

		if (resourceItem.getCompetency() == null || resourceItem.getCompetency().length() < 1) {
			resourceItem.getRscErrVO().setCompetencyError("Competency cannot be null, please select a value");
			return;
		}

		if (resourceItem.getSkillset() == null || resourceItem.getSkillset().length() < 1) {
			resourceItem.getRscErrVO().setSkillsetError("Skillset cannot be null, please select a value");
			return;
		}

		if (resourceItem.getBillability() == null || resourceItem.getBillability().length() < 1) {
			resourceItem.getRscErrVO().setBillabilityError("Billability cannot be null, please select a value");
			return;
		}

		if (resourceItem.getRequiredByDate() == null || resourceItem.getRequiredByDate().length() < 1) {
			resourceItem.getRscErrVO().setRequiredByDateError("RequiredByDate cannot be null, please select a value");
			return;
		}

		if (resourceItem.getBillingStartDate() == null || resourceItem.getBillingStartDate().length() < 1) {
			resourceItem.getRscErrVO()
					.setBillingStartDateError("BillingStartDate cannot be null, please select a value");
			return;
		}

		if (resourceItem.getBillingEndDate() == null || resourceItem.getBillingEndDate().length() < 1) {
			resourceItem.getRscErrVO().setBillingEndDateError("BillingEndDate cannot be null, please select a value");
			return;
		}

		if (resourceItem.getResourceStatus() == null || resourceItem.getResourceStatus().length() < 1) {
			resourceItem.getRscErrVO().setResourceStatusError("Resource Status cannot be null, please select a value");
			return;
		}

		// Field valid values validation...
		if (!StaticCache.isValidParentCustomer(resourceItem.getParentCustomerName())) {
			resourceItem.getRscErrVO().setParentCustomerNameError("Ivalid ParentCustomer...");
		}

		if (StaticCache.getRevenueItem(resourceItem.getOpptyProjectName()) == null) {
			resourceItem.getRscErrVO()
					.setOpptyProjectNameError("OpptyProjectName does not exist, create the project/oppty first.");
		}

		if (!StaticCache.isValidGrade(resourceItem.getGrade())) {
			resourceItem.getRscErrVO().setGradeError("Invalid ParentCustomer...");
		}

		if (!StaticCache.isValidLocation(resourceItem.getLocation())) {
			resourceItem.getRscErrVO().setLocationError("Invalid Location...");
		}

		if (!StaticCache.isValidCompetency(resourceItem.getCompetency())) {
			resourceItem.getRscErrVO().setCompetencyError("Invalid Competency...");
		} else {
			if (!StaticCache.isValidSkillset(resourceItem.getCompetency(), resourceItem.getSkillset())) {
				resourceItem.getRscErrVO().setSkillsetError("Invalid Skillset...");
			}
		}

		if (!StaticCache.isValidResourceStatus(resourceItem.getResourceStatus())) {
			resourceItem.getRscErrVO().setResourceStatusError("Invalid Resource Status...");
		}

		if (resourceItem.getConfidence() < 0 || resourceItem.getConfidence() > 1) {
			resourceItem.getRscErrVO().setConfidenceError("Invalid Confidence value, range is between 0% to 100%.");
		}

		// Other field validations...
		if (resourceItem.getOpptyProjectName().length() > 50) {
			resourceItem.getRscErrVO().setOpptyProjectNameError(
					"OpptyProjectName is > 50 chars (too big), use Remarks column for details. ");
		}

		if (resourceItem.getRemarks() != null && resourceItem.getRemarks().length() > 254) {
			resourceItem.getRscErrVO().setRemarksError("Remarks cannot be greater than 254 characters long ");
		}
		if (!StaticCache.canDEMModifyRecord(resourceItem.getParentCustomerName())) {
			resourceItem.getRscErrVO().setParentCustomerNameError(
					"The current logged in User cannot insert/update on a parent customer not assigned to them.");
		}

		ProjectVO revVO = StaticCache.getRevenueItem(resourceItem.getOpptyProjectName());
		if (revVO != null) {
			if (resourceItem.getParentCustomerName() == null
					|| !resourceItem.getParentCustomerName().equalsIgnoreCase(revVO.getParentCustomerName())) {
				resourceItem.getRscErrVO().setParentCustomerNameError(
						"Parent Customer in Resource Item is not the same Parent Customer of the project name selected, please correct it.");
			}
		}

		if (MonkeyConstants.FULFILLED.equalsIgnoreCase(resourceItem.getResourceStatus())) {
			if (resourceItem.getAssociateId() < 1) {
				resourceItem.getRscErrVO().setAssociateIdError(
						"Associate ID is must if resource status is fulfilled, please enter associate id.");
				return;
			}

			// DateUtility dateUtil = new
			// DateUtility(resourceItem.getLocation(),
			// resourceItem.getAssociateId());
			// LocalDate startDate =
			// dateUtil.getLocalStartDate(resourceItem.getBillingStartDate());
			// LocalDate endDate =
			// dateUtil.getLocalStartDate(resourceItem.getBillingEndDate());
			//
			// int[] monthRange = dateUtil.getMonthRange(startDate, endDate);
			//
			// for (int i = 0; i <= monthRange.length; i++) {
			// int key = resourceItem.getAssociateId() + (monthRange[i] *
			// 1000000);
			//
			// double fteCount = 0;
			// if (associateAllocation.size() > 0) {
			// Double dblFteCount = associateAllocation.get(key);
			// if (dblFteCount != null) {
			// fteCount = dblFteCount.doubleValue();
			// }
			// }
			//
			// //vimal
			// // dateUtil.getCalendarDays(startDate, endDate, monthStartDate,
			// // monthEndDate);
			//
			// fteCount += resourceItem.getFTECount();
			// if (fteCount > MonkeyConstants.MAX_ALLOCATION_FTE
			// &&
			// (revVO.getProjectTypeValue().contains(MonkeyConstants.BTM_PROJECT_TYPE)))
			// {
			// resourceItem.setErrorDetails(
			// "Associate aggregated allocation FTE is > 1.0, which cannot be
			// possible, please validate all BTM allocation for this
			// associate.");
			// return;
			// }
			// associateAllocation.put(key, fteCount);
			// }
		}
	}
}
