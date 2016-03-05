package com.helpermonkey.vo;

import java.util.HashMap;

import com.helpermonkey.common.MonkeyConstants;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ParentCustomerVO {

	public int Id;

	public String parentCustomerName = null;
	public int BillableHours;
	public double discount;
	public String vertical;

	public int DEMOffshoreId;
	public int DEMOnsiteId;

	public HashMap<String, Double> rateCardMap;

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		//if parent customer is being sent in then compare using the name, if not compare using id
		if(parentCustomerName != null){
			return this.parentCustomerName.equalsIgnoreCase(((ParentCustomerVO) obj).getParentCustomerName());
		}else{
			return this.Id == ((ParentCustomerVO) obj).getId();
		}
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (obj == null || this.parentCustomerName == null) {
	// return false;
	// }
	// return this.parentCustomerName.equals(((ParentCustomerVO)
	// obj).getParentCustomerName());
	// }

	public int getId() {
		return Id;
	}

	public ParentCustomerVO setId(int customerId) {
		this.Id = customerId;
		return this;
	}

	public String getParentCustomerName() {
		return parentCustomerName;
	}

	public ParentCustomerVO setParentCustomerName(String parentCustomerName) {
		this.parentCustomerName = parentCustomerName;
		return this;
	}

	public int getBillableHours() {
		return BillableHours;
	}

	public void setBillableHours(int billableHours) {
		this.BillableHours = billableHours;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	public String getVertical() {
		return vertical;
	}

	public void setVertical(String vertical) {
		this.vertical = vertical;
	}

	public int getDEMOffshoreId() {
		return DEMOffshoreId;
	}

	public void setDEMOffshoreId(int demOffshore) {
		this.DEMOffshoreId = demOffshore;
	}

	public int getDEMOnsiteId() {
		return DEMOnsiteId;
	}

	public void setDEMOnsiteId(int demOnsite) {
		this.DEMOnsiteId = demOnsite;
	}

	public ParentCustomerVO addRateCard(String projectRole, String location, double rateCard) {
		if(rateCardMap == null){
			rateCardMap = new HashMap<String, Double>();
		}
		this.rateCardMap.put(projectRole + "_" + location, new Double(rateCard));
		return this;
	}

	public double getRateCard(String projectRoleName, String location) {
		if(location.contains(MonkeyConstants.USA_LOCATION_VALUE)){
			location = MonkeyConstants.USA_LOCATION_VALUE;
		}else if(location.contains(MonkeyConstants.UK_LOCATION_VALUE)){
			location = MonkeyConstants.UK_LOCATION_VALUE;
		}else{
			location = MonkeyConstants.OFFSHORE_LOCATION_VALUE;
		}
		
		if (rateCardMap == null) {
			return 0;
		}
		
		Double rateCard = this.rateCardMap.get(projectRoleName + "_" + location);
		if (rateCard == null) {
			return 0;
		}

		return rateCard.doubleValue();
	}

}
