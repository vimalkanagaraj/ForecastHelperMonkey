package com.helpermonkey.vo;

import com.helpermonkey.common.MonkeyConstants;
import com.helpermonkey.util.StaticCache;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ResourceVO {

	public int rowNumber;

	public String changeFlag;
	
	//Type="Edm.Int32" Nullable="false" />
	public int Id;
	
	public String ParentCustomerName;
	
    public String OpptyProjectName;
    
    //Type="Edm.String" Nullable="true"
    public String Grade;
    
    //Type="Edm.String" Nullable="true" 
    public String location;
    
    public double rateCard;
    
    //Type="Edm.String" Nullable="true"
    public String Competency;
    
    //Type="Edm.String" Nullable="true"
    public String Skillset;
    
    //Type="Edm.Double" Nullable="true"
    public double FTECount;
    
    public String Billability;
    
    //Type="Edm.DateTime" Nullable="true"
    public String RequiredByDate;
    
    //Type="Edm.DateTime" Nullable="true"
    public String BillingStartDate;
    
    //Type="Edm.DateTime" Nullable="true"
    public String BillingEndDate;
    
    //Type="Edm.Double" Nullable="true"
    public double Confidence;
    
    //Type="Edm.String" Nullable="true"
    public int SOUniqueNumber;
    
    //Type="Edm.Double" Nullable="true"
    public int AssociateId;
    
    public String AssociateName;
    
    //Type="Edm.String" Nullable="true"
    public String ResourceStatus;
    
    //Type="Edm.String" Nullable="true" - BIG TEXT
    public String Remarks;
    
    public int demOffshoreId;
    
    public int demOnsiteId;
    
    public CalculatedNumbersVO calculatedFieldsVO;
    
    public ResourceErrorVO resourceErrorVO;
    
   
	public ResourceVO() throws Exception {
	}
	
	public int getOpptyProjectId(){
		ProjectVO revVO = StaticCache.getRevenueItem(this.OpptyProjectName); 
		if( revVO != null){
			return revVO.getId();
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.Id == ((ResourceVO) obj).getId());
	}
	
	
	
	public String getGrade() {
		return Grade;
	}

	public void setGrade(String grade) {
		Grade = grade;
	}

	public ResourceErrorVO getRscErrVO() {
		if(resourceErrorVO == null){
			resourceErrorVO = new ResourceErrorVO();
		}
		return resourceErrorVO;
	}

	public void setRscErrVO(ResourceErrorVO resourceErrorVO) {
		this.resourceErrorVO = resourceErrorVO;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}
	
	public String getLocationCountry() {
		if (MonkeyConstants.USA_LOCATION_VALUE.equalsIgnoreCase(getLocation())) {
			return MonkeyConstants.USA_LOCATION_VALUE;
		} else if (getLocation().contains(MonkeyConstants.UK_LOCATION_VALUE)) {
			return MonkeyConstants.UK_LOCATION_VALUE;
		} else {
			return MonkeyConstants.OFFSHORE_LOCATION_VALUE;
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ResourceItemVO [rowNumber=");
		builder.append(rowNumber);
		builder.append(", changeFlag=");
		builder.append(changeFlag);
		builder.append(", Id=");
		builder.append(Id);
		builder.append(", ParentCustomerName=");
		builder.append(ParentCustomerName);
		builder.append(", OpptyProjectName=");
		builder.append(OpptyProjectName);
		builder.append(", ProjectRole=");
		builder.append(Grade);
		builder.append(", location=");
		builder.append(location);
		builder.append(", Competency=");
		builder.append(Competency);
		builder.append(", Skillset=");
		builder.append(Skillset);
		builder.append(", FTECount=");
		builder.append(FTECount);
		builder.append(", Billability=");
		builder.append(Billability);
		builder.append(", RequiredByDate=");
		builder.append(RequiredByDate);
		builder.append(", BillingStartDate=");
		builder.append(BillingStartDate);
		builder.append(", BillingEndDate=");
		builder.append(BillingEndDate);
		builder.append(", Confidence=");
		builder.append(Confidence);
		builder.append(", SOUniqueNumber=");
		builder.append(SOUniqueNumber);
		builder.append(", AssociateId=");
		builder.append(AssociateId);
		builder.append(", AssociateName=");
		builder.append(AssociateName);
		builder.append(", ResourceStatus=");
		builder.append(ResourceStatus);
		builder.append(", Remarks=");
		builder.append(Remarks);
		builder.append(", demOffshoreId=");
		builder.append(demOffshoreId);
		builder.append(", demOnsiteId=");
		builder.append(demOnsiteId);
		builder.append(", calculatedFieldsVO=");
		builder.append(calculatedFieldsVO);
		builder.append("]");
		return builder.toString();
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getChangeFlag() {
		return changeFlag;
	}
	
	public void calculateChangeFlag(){
		if (getRscErrVO().toString() != null && getRscErrVO().toString().length() > 1) {
			setChangeFlag(MonkeyConstants.CHANGE_FLAG_ERROR);
		}
	}

	public void setChangeFlag(String changeFlag) {
		this.changeFlag = changeFlag;
	}

	public String getParentCustomerName() {
		return ParentCustomerName;
	}

	public void setParentCustomerName(String parentCustomerName) {
		ParentCustomerName = parentCustomerName;
	}

	public String getOpptyProjectName() {
		return OpptyProjectName;
	}

	public void setOpptyProjectName(String opptyProjectName) {
		OpptyProjectName = opptyProjectName;
	}

	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}

	public String getCompetency() {
		return Competency;
	}

	public void setCompetency(String competency) {
		Competency = competency;
	}

	public String getSkillset() {
		return Skillset;
	}

	public void setSkillset(String skillset) {
		Skillset = skillset;
	}

	public double getFTECount() {
		return FTECount;
	}

	public void setFTECount(double fTECount) {
		FTECount = fTECount;
	}
	
	

	public String getBillability() {
		return Billability;
	}

	public void setBillability(String billability) {
		Billability = billability;
	}

	public String getRequiredByDate() {
		return RequiredByDate;
	}

	public void setRequiredByDate(String requiredByDate) {
		RequiredByDate = requiredByDate;
	}

	public String getBillingStartDate() {
		return BillingStartDate;
	}

	public void setBillingStartDate(String billingStartDate) {
		BillingStartDate = billingStartDate;
	}

	public String getBillingEndDate() {
		return BillingEndDate;
	}

	public void setBillingEndDate(String billingEndDate) {
		BillingEndDate = billingEndDate;
	}

	public double getConfidence() {
		return Confidence;
	}

	public void setConfidence(double confidence) {
		Confidence = confidence;
	}

	public int getSOUniqueNumber() {
		return SOUniqueNumber;
	}

	public void setSOUniqueNumber(int soUniqueNumber) {
		SOUniqueNumber = soUniqueNumber;
	}

	public int getAssociateId() {
		return AssociateId;
	}

	public void setAssociateId(int associateId) {
		AssociateId = associateId;
	}

	public String getResourceStatus() {
		return ResourceStatus;
	}

	public void setResourceStatus(String resourceStatus) {
		ResourceStatus = resourceStatus;
	}

	public String getRemarks() {
		return Remarks;
	}

	public void setRemarks(String remarks) {
		Remarks = remarks;
	}

	public String getAssociateName() {
		return AssociateName;
	}

	public void setAssociateName(String associateName) {
		AssociateName = associateName;
	}

	public int getDemOffshoreId() {
		return demOffshoreId;
	}

	public void setDemOffshoreId(int demOffshoreId) {
		this.demOffshoreId = demOffshoreId;
	}

	public int getDemOnsiteId() {
		return demOnsiteId;
	}

	public void setDemOnsiteId(int demOnsiteId) {
		this.demOnsiteId = demOnsiteId;
	}

	public double getRateCard() {
		return rateCard;
	}

	public void setRateCard(double rateCard) {
		this.rateCard = rateCard;
	}

	public CalculatedNumbersVO getCalculatedFieldsVO() {
		return calculatedFieldsVO;
	}

	public void setCalculatedFieldsVO(CalculatedNumbersVO calculatedFieldsVO) {
		this.calculatedFieldsVO = calculatedFieldsVO;
	}
	
	

	

}
