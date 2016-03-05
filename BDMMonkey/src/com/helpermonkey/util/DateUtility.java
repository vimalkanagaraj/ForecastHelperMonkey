package com.helpermonkey.util;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeConstants;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import com.helpermonkey.common.MonkeyConstants;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class DateUtility {

	public static final LocalDate Jan1 = new LocalDate(MonkeyConstants.CURRENT_YR, 01, 01);
	public static final LocalDate Dec31 = new LocalDate(MonkeyConstants.CURRENT_YR, 12, 31);

	public String location = "";
	public int associateId = 0;

	public DateUtility() throws Exception {
		// Date stDt = new Date(resourceVO.getBillingStartDate());
		// LocalDate startDate = new LocalDate(stDt.getYear(), stDt.getMonth(),
		// stDt.getDate());
		//
		// Date endDt = new Date(resourceVO.getBillingEndDate());
		// LocalDate endDate = new LocalDate(endDt.getYear(), endDt.getMonth(),
		// endDt.getDate());
		try {
			// load only once during startup
			if (StaticCache.weekDayMap.isEmpty()) {
				int dayOfWeek = Jan1.getDayOfWeek();
				for (int dayCounter = 1; dayCounter <= Dec31.getDayOfYear(); dayCounter++) {
					StaticCache.weekDayMap.put(new Integer(dayCounter), new Integer(dayOfWeek));
					if (dayOfWeek == DateTimeConstants.SUNDAY) {
						dayOfWeek = 0;
					}
					dayOfWeek++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public DateUtility(String location, int associateId) throws Exception {
		this();
		this.location = location;
		this.associateId = associateId;
	}

	public LocalDate getLocalStartDate(String date) {
		String[] strBuf = StringUtils.split(date, MonkeyConstants.DATE_SEPERATOR);
		LocalDate localDate = new LocalDate(new Integer(strBuf[0]).intValue(), new Integer(strBuf[1]).intValue(),
				new Integer(strBuf[2]).intValue());
		return localDate;
	}
	
	public static final int MONTH_DAY1 = 1;
	public double getCalendarDayRatio(LocalDate startDate, LocalDate endDate, int month) {
		LocalDate monthStartDate = new LocalDate(MonkeyConstants.CURRENT_YR, month, MONTH_DAY1);
		LocalDate monthEndDate = getMonthEndDate(month);
		int daysInMonth = Days.daysBetween(monthStartDate, monthEndDate).getDays() + 1;
		int allocationCalendarDays = getCalendarDays(startDate, endDate, monthStartDate, monthEndDate);
		
		double monthlyCostRatio = ((double)allocationCalendarDays / daysInMonth);
		
		return monthlyCostRatio;
	}
	
	public int getCalendarDays(LocalDate startDate, LocalDate endDate, int month) {
		LocalDate monthStartDate = new LocalDate(MonkeyConstants.CURRENT_YR, month, 01);
		LocalDate monthEndDate = getMonthEndDate(month);
		return getCalendarDays(startDate, endDate, monthStartDate, monthEndDate);
	}

	public int getCalendarDays(LocalDate startDate, LocalDate endDate, LocalDate monthStartDate,
			LocalDate monthEndDate) {
		int calendarDays = 0;
		if (startDate.isBefore(monthEndDate) && endDate.isAfter(monthStartDate)) {
			if (endDate.isBefore(monthEndDate)) {
				if (startDate.isBefore(monthStartDate)) {
					calendarDays = Days.daysBetween(monthStartDate, endDate).getDays();
				} else {
					calendarDays = Days.daysBetween(startDate, endDate).getDays();
				}
			} else {
				if (startDate.isBefore(monthStartDate)) {
					calendarDays = Days.daysBetween(monthStartDate, monthEndDate).getDays();
				} else {
					calendarDays = Days.daysBetween(startDate, monthEndDate).getDays();
				}
			}
			// we are adding one day because the getDays method does not include
			// the
			// startDate, but we need it to be inclusive of the start date,
			// hence this fix.
			// BUT we only add if there is at least one calendar day of
			// allocation not just for every month...
			++calendarDays;
		}
		return calendarDays;
	}
	
	public int getBillableDays(LocalDate startDate, LocalDate endDate, int month) {
		return getBillableDays(startDate,endDate, new LocalDate(MonkeyConstants.CURRENT_YR, month, 01),getMonthEndDate(month));
	}

	// startDate = 14-Jan-2015
	// endDate = 14-Feb-2015
	public int getBillableDays(LocalDate startDate, LocalDate endDate, LocalDate monthStartDate,
			LocalDate monthEndDate) {
		int calendarDays = getCalendarDays(startDate, endDate, monthStartDate, monthEndDate);
		if (calendarDays == 0)
			return calendarDays;

		int noOfBillingDays = 0;
		int dayOfYrCounter = 0;
		if (startDate.isAfter(monthStartDate)) {
			dayOfYrCounter = startDate.getDayOfYear();
		} else {
			dayOfYrCounter = monthStartDate.getDayOfYear();
		}

		// get the number of working days (mon - fri)
		ArrayList<Integer> holidaysList = (ArrayList<Integer>) StaticCache.locationHolidays.get(this.location);

		while (calendarDays > 0) {
			// check the workdays only if if the current day of the year is not
			// a declared holiday OR the location does not have any holidays for
			if (holidaysList == null || (!holidaysList.contains(dayOfYrCounter))) {
				int dayOfTheWeek = (Integer) StaticCache.weekDayMap.get(dayOfYrCounter).intValue();
				if (dayOfTheWeek == DateTimeConstants.MONDAY) {
					noOfBillingDays++;
				}
				if (dayOfTheWeek == DateTimeConstants.TUESDAY) {
					noOfBillingDays++;
				}
				if (dayOfTheWeek == DateTimeConstants.WEDNESDAY) {
					noOfBillingDays++;
				}
				if (dayOfTheWeek == DateTimeConstants.THURSDAY) {
					noOfBillingDays++;
				}
				if (dayOfTheWeek == DateTimeConstants.FRIDAY) {
					noOfBillingDays++;
				}

			}
			calendarDays--;
			dayOfYrCounter++;
		}

		// now filter out the declared holidays

		return noOfBillingDays;

	}

	public int negateAssociateVacation(int billableDays) {
		// check if the associate has applied for any vacation
		ArrayList<Integer> associateVacation = StaticCache.vacations.get(associateId);
		if(associateVacation == null){
			return billableDays;
		}

		// get the number of working days (mon - fri)
		ArrayList<Integer> holidaysList = (ArrayList<Integer>) StaticCache.locationHolidays.get(this.location);

		for (int i = 0; i < associateVacation.size(); i++) {
			int vacationDayOfYr = associateVacation.get(i);
			if (holidaysList == null || (!holidaysList.contains(vacationDayOfYr))) {
				billableDays--;
			}
		}

		return billableDays;
	}
	
	public int[] getMonthRange(LocalDate startDate, LocalDate endDate){
		boolean startRangeFound = false;
		int[] range = new int[12];
		int counter = 0;
		
		for(int i=1;i<=12;i++){
			//i is the month
			LocalDate monthStartDate1 = new LocalDate(MonkeyConstants.CURRENT_YR, i, 01);
			LocalDate monthEndDate1 = getMonthEndDate(i);
			if(startDate.isAfter(monthStartDate1) && startDate.isBefore(monthEndDate1)){
				range[counter] = i;
				startRangeFound = true;
			}
			if(startRangeFound && endDate.isBefore(monthEndDate1)){
				range[++counter] = i;
			}
		}
		
		return range;
	}

	public LocalDate getMonthEndDate(int monthNumber) {
		
		LocalDate monthEndDate = null;
		
		switch(monthNumber){
		case 1:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 31);
			break;
		case 2:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 28);
			break;
		case 3:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 31);
			break;
		case 4:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 30);
			break;
		case 5:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 31);
			break;
		case 6:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 30);
			break;
		case 7:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 31);
			break;
		case 8:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 31);
			break;
		case 9:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 30);
			break;
		case 10:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 31);
			break;
		case 11:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 30);
			break;
		case 12:
			monthEndDate = new LocalDate(MonkeyConstants.CURRENT_YR, monthNumber, 31);
			break;
		}
		
		return monthEndDate;
	}

	
	public LocalDate Jan31 = new LocalDate(MonkeyConstants.CURRENT_YR, 01, 31);
	
	public int getJanBillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Jan1, Jan31);
		return billableDays;
	}

	public LocalDate Feb1 = new LocalDate(MonkeyConstants.CURRENT_YR, 02, 01);
	public LocalDate FebEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 02, 28);
	public int getFebBillableDays(LocalDate startDate, LocalDate endDate) {
		int billableDays = getBillableDays(startDate, endDate, Feb1, FebEnd);
		return billableDays;
	}

	public LocalDate Mar1 = new LocalDate(MonkeyConstants.CURRENT_YR, 03, 01);
	public LocalDate MarEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 03, 31);
	public int getMarbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Mar1, MarEnd);
		return billableDays;
	}

	public LocalDate Apr1 = new LocalDate(MonkeyConstants.CURRENT_YR, 04, 01);
	public LocalDate AprEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 04, 30);
	public int getAprbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Apr1, AprEnd);
		return billableDays;
	}

	public LocalDate May1 = new LocalDate(MonkeyConstants.CURRENT_YR, 05, 01);
	public LocalDate MayEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 05, 31);
	public int getMaybillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, May1, MayEnd);
		return billableDays;
	}

	public LocalDate Jun1 = new LocalDate(MonkeyConstants.CURRENT_YR, 06, 01);
	public LocalDate JunEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 06, 30);
	public int getJunbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Jun1, JunEnd);
		return billableDays;
	}

	public LocalDate Jul1 = new LocalDate(MonkeyConstants.CURRENT_YR, 07, 01);
	public LocalDate JulEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 07, 31);
	public int getJulbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Jul1, JulEnd);
		return billableDays;
	}

	public LocalDate Aug1 = new LocalDate(MonkeyConstants.CURRENT_YR, 8, 01);
	public LocalDate AugEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 8, 31);
	public int getAugbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Aug1, AugEnd);
		return billableDays;
	}

	public LocalDate Sep1 = new LocalDate(MonkeyConstants.CURRENT_YR, 9, 01);
	public LocalDate SepEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 9, 30);
	public int getSepbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Sep1, SepEnd);
		return billableDays;
	}

	public LocalDate Oct1 = new LocalDate(MonkeyConstants.CURRENT_YR, 10, 01);
	public LocalDate OctEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 10, 31);
	public int getOctbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Oct1, OctEnd);
		return billableDays;
	}

	public LocalDate Nov1 = new LocalDate(MonkeyConstants.CURRENT_YR, 11, 01);
	public LocalDate NovEnd = new LocalDate(MonkeyConstants.CURRENT_YR, 11, 30);
	public int getNovbillableDays(LocalDate startDate, LocalDate endDate) {

		int billableDays = getBillableDays(startDate, endDate, Nov1, NovEnd);
		return billableDays;
	}

	public LocalDate Dec1 = new LocalDate(MonkeyConstants.CURRENT_YR, 12, 01);
	public int getDecbillableDays(LocalDate startDate, LocalDate endDate) {
		int billableDays = getBillableDays(startDate, endDate, Dec1, Dec31);
		return billableDays;
	}

}
