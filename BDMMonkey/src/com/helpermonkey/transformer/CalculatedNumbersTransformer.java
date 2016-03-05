package com.helpermonkey.transformer;

import org.apache.olingo.client.api.domain.ClientObjectFactory;
import org.joda.time.LocalDate;

import com.helpermonkey.util.DateUtility;
import com.helpermonkey.util.StaticCache;
import com.helpermonkey.vo.CalculatedNumbersVO;
import com.helpermonkey.vo.ParentCustomerVO;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class CalculatedNumbersTransformer extends AbstractTransformer {

	// private static final Logger logger =
	// LoggerFactory.getLogger(CalculatedNumbersTransformer.class);

	private static final String BILLED = "Billed";

	ClientObjectFactory objFactory = null;

	DateUtility dateUtil = null;

	public static final String FQNAME_CALCULATED_ITEM = "Microsoft.SharePoint.DataService.CalculatedDataItem";

	public CalculatedNumbersTransformer(ClientObjectFactory objectFactory) throws Exception {
		this.objFactory = objectFactory;
		this.dateUtil = new DateUtility();
	}

	public CalculatedNumbersVO calculateTheNumbers(CalculatedNumbersVO calcVO)
			throws Exception {
		if (calcVO == null) {
			calcVO = new CalculatedNumbersVO();
			calcVO.setRowNumber(calcVO.getRowNumber());
		}

		// be careful with the objFactory.newPrimitiveValueBuilder() this is
		// actually a singleton and returns
		// a static instance, so everytime a new value needs to be set we need
		// to get a new instance...
		ParentCustomerVO pcustVO = StaticCache.getParentCustomerByName(calcVO.getResourceItemVO().getParentCustomerName());

		int billableHours = pcustVO.getBillableHours();
		double rateCard = calcVO.getResourceItemVO().getRateCard();
		double fteCount = calcVO.getResourceItemVO().getFTECount();
		double customerDiscount = pcustVO.getDiscount();

		DateUtility dateUtil = new DateUtility(calcVO.getResourceItemVO().getLocation(), calcVO.getResourceItemVO().getAssociateId());
		LocalDate startDate = dateUtil.getLocalStartDate(calcVO.getResourceItemVO().getBillingStartDate());
		LocalDate endDate = dateUtil.getLocalStartDate(calcVO.getResourceItemVO().getBillingEndDate());
		
		for (int month = 1; month < 12; month++) {

			int billableDays = dateUtil.getBillableDays(startDate, endDate, month);
			int billedDays = dateUtil.negateAssociateVacation(billableDays);
			double revenue = 0;
			if(BILLED.equalsIgnoreCase(calcVO.getResourceItemVO().getBillability())){
				revenue = billedDays * billableHours * rateCard * fteCount * (1 - (customerDiscount / 100));
			}else{
				revenue = 0;
			}

			calcVO.setBilledDays(month, billedDays);
			calcVO.setBilledFte(month, fteCount, billableDays);
			calcVO.setRevenue(month, revenue);
			calcVO.calculateCostAndCP(month, startDate, endDate);
		}

		// Create the calc vo to write into the excel
		calcVO.calculateQtrly();

		return calcVO;
	}

}
