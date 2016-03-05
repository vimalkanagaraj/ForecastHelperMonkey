package com.helpermonkey.vo;

import java.util.HashMap;

import org.joda.time.LocalDate;

import com.helpermonkey.util.DateUtility;
import com.helpermonkey.util.StaticCache;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class CalculatedNumbersVO {
	
	int id;

	int rowNumber;

	public ResourceVO resourceVO;

	public HashMap<Integer, Double> revenueMap = new HashMap<Integer, Double>();

	public HashMap<Integer, Double> billedFteMap = new HashMap<Integer, Double>();
	
	public HashMap<Integer, Integer> billedDaysMap = new HashMap<Integer, Integer>();

	public HashMap<Integer, Double> directCostMap = new HashMap<Integer, Double>();
	
	public HashMap<Integer, Double> indirectCostMap = new HashMap<Integer, Double>();

	public HashMap<Integer, Double> cpDollarMap = new HashMap<Integer, Double>();

	public HashMap<Integer, Double> cpPercentMap = new HashMap<Integer, Double>();
	
	public static final int JAN = 1;
	public static final int FEB = 2;
	public static final int MAR = 3;
	public static final int APR = 4;
	public static final int MAY = 5;
	public static final int JUN = 6;
	public static final int JUL = 7;
	public static final int AUG = 8;
	public static final int SEP = 9;
	public static final int OCT = 10;
	public static final int NOV = 11;
	public static final int DEC = 12;
	public static final int Q1 = 13;
	public static final int Q2 = 14;
	public static final int Q3 = 15;
	public static final int Q4 = 16;
	public static final int FY = 17;


	public CalculatedNumbersVO() throws Exception {
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}
	
	public void setResourceItemVO(ResourceVO resourceItem) {
		this.resourceVO = resourceItem;
		this.id = resourceItem.getId();
	}
	
	public ResourceVO getResourceItemVO() {
		return this.resourceVO;
	}
	
	/*** revenue and other calculated setter & getter methods ***/
	
	public void validateInput(int month){
		if(month < 1 || month > 17){
			throw new IllegalArgumentException("Month cannot be more than 12 or even including quarters cannot be more than 17.");
		}
	}

	public void setRevenue(int month, double revenue) throws Exception{
		validateInput(month);
		this.revenueMap.put(month, Math.floor(revenue));
	}
	
	public double getRevenue(int month) throws Exception{
		validateInput(month);
		
		Object rev = this.revenueMap.get(month);
		if(rev != null){
			return ((Double)rev).doubleValue();
		}else{
			return 0;
		}
	}
	
	public double getWeightedRevenue(int month) throws Exception{
		validateInput(month);
		
		Object rev = this.revenueMap.get(month);
		if(rev != null){
			return ((Double)rev).doubleValue() * resourceVO.getConfidence();
		}else{
			return 0;
		}
	}
	
	public void setBilledDays(int month, int billedDays) throws Exception{
		validateInput(month);
		this.billedDaysMap.put(month, billedDays);
	}
	
	public int getBilledDays(int month) throws Exception{
		validateInput(month);
		
		Object billedDays = this.billedDaysMap.get(month);
		if(billedDays != null){
			return ((Integer)billedDays).intValue();
		}else{
			return 0;
		}
	}
	
	public void setBilledFte(int month, double fteCount, int billableDays) throws Exception {
		validateInput(month);
		
		if (billableDays == 0 || fteCount == 0) {
			this.billedFteMap.put(month, 0.0d);
			return;
		}

		double billedFte = (getBilledDays(month) * fteCount) / billableDays;
		this.billedFteMap.put(month, billedFte);
	}
	
	public double getBilledFte(int month) throws Exception{
		validateInput(month);
		
		Object billedFte = this.billedFteMap.get(month);
		if(billedFte != null){
			return ((Double)billedFte).doubleValue();
		}else{
			return 0;
		}
	}
	
	public void calculateCostAndCP(int month, LocalDate startDate, LocalDate endDate) throws Exception {
		validateInput(month);
		
		DateUtility dateUtil = new DateUtility(resourceVO.getLocation(), resourceVO.getAssociateId());

		double calDayRatio =  dateUtil.getCalendarDayRatio(startDate, endDate, month);
		
		//get monthly direct cost based on standard cost rate card...
		double monthlyDirectCost = StaticCache.getMonthlyDirectCost(resourceVO.getGrade(), resourceVO.getLocation());
		double directCost = calDayRatio * resourceVO.getFTECount() * monthlyDirectCost;
		this.directCostMap.put(month, directCost);

		//get the indirect cost like infrastructure, seat cost etc.
		double monthlyIndirectCost = StaticCache.getMonthlyIndirectCost(resourceVO.getGrade(), resourceVO.getLocation());
		double indirectCost = calDayRatio * resourceVO.getFTECount() * monthlyIndirectCost;
		this.indirectCostMap.put(month, indirectCost);
		
		double totalCost = directCost + indirectCost;

		double cpDollar = getWeightedRevenue(month) - totalCost;
		this.cpDollarMap.put(month, cpDollar);

		double cpPercent = 0;
		if (getWeightedRevenue(month) != 0) {
			cpPercent = cpDollar / getWeightedRevenue(month);
		}
		this.cpPercentMap.put(month, cpPercent);
	}
	
	public double getDirectCost(int month) throws Exception{
		validateInput(month);
		
		Object directCost = this.directCostMap.get(month);
		if(directCost != null){
			return ((Double)directCost).doubleValue();
		}else{
			return 0;
		}
	}
	
	public double getIndirectCost(int month) throws Exception{
		validateInput(month);
		
		Object indirectCost = this.indirectCostMap.get(month);
		if(indirectCost != null){
			return ((Double)indirectCost).doubleValue();
		}else{
			return 0;
		}
	}
	
	public double getCPDollar(int month) throws Exception{
		validateInput(month);
		
		Object cpDollar = this.cpDollarMap.get(month);
		if(cpDollar != null){
			return ((Double)cpDollar).doubleValue();
		}else{
			return 0;
		}
	}
	
	public double getCPPercent(int month) throws Exception{
		validateInput(month);
		
		Object cpPercent = this.cpPercentMap.get(month);
		if(cpPercent != null){
			return ((Double)cpPercent).doubleValue();
		}else{
			return 0;
		}
	}
	
	public void calculateQtrly() throws Exception{
		//set Q1 Revenue, Q1 is treated as month 13.
		this.revenueMap.put(Q1, (Math.floor(getRevenue(JAN) + getRevenue(FEB) + getRevenue(MAR))));
		//set Q2 Revenue, Q2 is treated as month 14.
		this.revenueMap.put(Q2, (Math.floor(getRevenue(APR) + getRevenue(MAY) + getRevenue(JUN))));
		//set Q3 Revenue, Q3 is treated as month 15.
		this.revenueMap.put(Q3, (Math.floor(getRevenue(JUL) + getRevenue(AUG) + getRevenue(SEP))));
		//set Q4 Revenue, Q4 is treated as month 16.
		this.revenueMap.put(Q4, (Math.floor(getRevenue(OCT) + getRevenue(NOV) + getRevenue(DEC))));
		//set FY Revenue, FY is treated as month 17.
		setRevenue(FY, (Math.floor(getRevenue(Q1) + getRevenue(Q2) + getRevenue(Q3) + getRevenue(Q4))));

		this.billedDaysMap.put(Q1, (getBilledDays(JAN) + getBilledDays(FEB) + getBilledDays(MAR)));
		this.billedDaysMap.put(Q2, (getBilledDays(APR) + getBilledDays(MAY) + getBilledDays(JUN)));
		this.billedDaysMap.put(Q3, (getBilledDays(JUL) + getBilledDays(AUG) + getBilledDays(SEP)));
		this.billedDaysMap.put(Q4, (getBilledDays(OCT) + getBilledDays(NOV) + getBilledDays(DEC)));
		this.billedDaysMap.put(FY, (getBilledDays(Q1) + getBilledDays(Q2) + getBilledDays(Q3) + getBilledDays(Q4)));

		this.billedFteMap.put(Q1, (getBilledFte(JAN) + getBilledFte(FEB) + getBilledFte(MAR)));
		this.billedFteMap.put(Q2, (getBilledFte(APR) + getBilledFte(MAY) + getBilledFte(JUN)));
		this.billedFteMap.put(Q3, (getBilledFte(JUL) + getBilledFte(AUG) + getBilledFte(SEP)));
		this.billedFteMap.put(Q4, (getBilledFte(OCT) + getBilledFte(NOV) + getBilledFte(DEC)));
		this.billedFteMap.put(FY, (getBilledFte(Q1) + getBilledFte(Q2) + getBilledFte(Q3) + getBilledDays(Q4)));

		this.directCostMap.put(Q1, (getDirectCost(JAN) + getDirectCost(FEB) + getDirectCost(MAR)));
		this.directCostMap.put(Q2, (getDirectCost(APR) + getDirectCost(MAY) + getDirectCost(JUN)));
		this.directCostMap.put(Q3, (getDirectCost(JUL) + getDirectCost(AUG) + getDirectCost(SEP)));
		this.directCostMap.put(Q4, (getDirectCost(OCT) + getDirectCost(NOV) + getDirectCost(DEC)));
		this.directCostMap.put(FY, (getDirectCost(Q1) + getDirectCost(Q2) + getDirectCost(Q3) + getDirectCost(Q4)));

		this.indirectCostMap.put(Q1, (getIndirectCost(JAN) + getIndirectCost(FEB) + getIndirectCost(MAR)));
		this.indirectCostMap.put(Q2, (getIndirectCost(APR) + getIndirectCost(MAY) + getIndirectCost(JUN)));
		this.indirectCostMap.put(Q3, (getIndirectCost(JUL) + getIndirectCost(AUG) + getIndirectCost(SEP)));
		this.indirectCostMap.put(Q4, (getIndirectCost(OCT) + getIndirectCost(NOV) + getIndirectCost(DEC)));
		this.indirectCostMap.put(FY, (getIndirectCost(Q1) + getIndirectCost(Q2) + getIndirectCost(Q3) + getIndirectCost(Q4)));

		this.cpDollarMap.put(Q1, (getCPDollar(JAN) + getCPDollar(FEB) + getCPDollar(MAR)));
		this.cpDollarMap.put(Q2, (getCPDollar(APR) + getCPDollar(MAY) + getCPDollar(JUN)));
		this.cpDollarMap.put(Q3, (getCPDollar(JUL) + getCPDollar(AUG) + getCPDollar(SEP)));
		this.cpDollarMap.put(Q4, (getCPDollar(OCT) + getCPDollar(NOV) + getCPDollar(DEC)));
		this.cpDollarMap.put(FY, (getCPDollar(Q1) + getCPDollar(Q2) + getCPDollar(Q3) + getCPDollar(Q4)));

		
		if (getWeightedRevenue(Q1) != 0) {
			this.cpPercentMap.put(Q1, (getCPDollar(Q1) / getWeightedRevenue(Q1)));
		}
		if (getWeightedRevenue(Q2) != 0) {
			this.cpPercentMap.put(Q2, (getCPDollar(Q2) / getWeightedRevenue(Q2)));
		}
		if (getWeightedRevenue(Q3) != 0) {
			this.cpPercentMap.put(Q3, (getCPDollar(Q3) / getWeightedRevenue(Q3)));
		}
		if (getWeightedRevenue(Q4) != 0) {
			this.cpPercentMap.put(Q4, (getCPDollar(Q4) / getWeightedRevenue(Q4)));
		}
		if (getWeightedRevenue(FY) != 0) {
			this.cpPercentMap.put(FY, (getCPDollar(FY) / getWeightedRevenue(FY)));
		}
	}
}
