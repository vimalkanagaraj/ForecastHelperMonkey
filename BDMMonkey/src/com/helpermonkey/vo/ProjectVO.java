package com.helpermonkey.vo;

import java.util.ArrayList;

import com.helpermonkey.common.MonkeyConstants;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ProjectVO {

	public int rowNumber;
	
	public String changeFlag;

	// Type="Edm.Int32" Nullable="false"
	public int Id;

	// Type="Edm.String" Nullable="true"
	public String OpptyProjectName; // entry
	
	// Type="Edm.String" Nullable="true"
	public long ESAProjectId; // entry

	// Type="Edm.Int32" Nullable="true"
	public String ParentCustomerName; // entry

	// Type="Edm.String" Nullable="true"
	public String ProjectTypeValue;

	// Type="Edm.String" Nullable="true"
//	public String CategoryValue; // entry

	// Type="Edm.String" Nullable="true" - BIG TEXT
	public String Remarks;

	// Type="Edm.String" Nullable="true"
	public String StatusValue; // entry
	
    public int demOffshoreId;
    
    public int demOnsiteId;
    
    public ArrayList<ResourceVO> resources = new ArrayList<ResourceVO>();
    
	public ProjectErrorVO prjErrVO;
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(OpptyProjectName == null)
			return false;
		//overriding the default method and providing a custom equals to say when the name is same they are equal.
		return this.OpptyProjectName.equalsIgnoreCase(((ProjectVO) obj).getOpptyProjectName());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RevenueItemVO [rowNumber=");
		builder.append(rowNumber);
		builder.append(", changeFlag=");
		builder.append(changeFlag);
		builder.append(", Id=");
		builder.append(Id);
		builder.append(", OpptyProjectName=");
		builder.append(OpptyProjectName);
		builder.append(", ESAProjectId=");
		builder.append(ESAProjectId);
		builder.append(", ParentCustomerName=");
		builder.append(ParentCustomerName);
		builder.append(", ProjectTypeValue=");
		builder.append(ProjectTypeValue);
//		builder.append(", CategoryValue=");
//		builder.append(CategoryValue);
//		builder.append(", BidCP=");
//		builder.append(BidCP);
		builder.append(", Remarks=");
		builder.append(Remarks);
		builder.append(", StatusValue=");
		builder.append(StatusValue);
		builder.append("]");
		return builder.toString();
	}
	
	

	public ProjectErrorVO getPrjErrVO() {
		if(prjErrVO == null){
			prjErrVO = new ProjectErrorVO();
		}
		return prjErrVO;
	}

	public void setPrjErrVO(ProjectErrorVO prjErrVO) {
		this.prjErrVO = prjErrVO;
	}

	public int getId() {
		return Id;
	}

	public void setId(int id) {
		Id = id;
	}

	public String getOpptyProjectName() {
		return OpptyProjectName;
	}

	public ProjectVO setOpptyProjectName(String opptyProjectName) {
		OpptyProjectName = opptyProjectName;
		return this;
	}

	public long getESAProjectId() {
		return ESAProjectId;
	}

	public void setESAProjectId(long projectID) {
		ESAProjectId = projectID;
	}

	public String getProjectTypeValue() {
		return ProjectTypeValue;
	}

	public void setProjectTypeValue(String projectTypeValue) {
		ProjectTypeValue = projectTypeValue;
	}

	public String getRemarks() {
		return Remarks;
	}

	public void setRemarks(String remarks) {
		Remarks = remarks;
	}

	public String getStatusValue() {
		return StatusValue;
	}

	public void setStatusValue(String statusValue) {
		StatusValue = statusValue;
	}


	public String getChangeFlag() {
		return changeFlag;
	}
	
	public void calculateChangeFlag(){
		if (getPrjErrVO().toString() != null && getPrjErrVO().toString().length() > 1) {
			setChangeFlag(MonkeyConstants.CHANGE_FLAG_ERROR);
		}
	}

	public void setChangeFlag(String changeFlag) {
		this.changeFlag = changeFlag;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getParentCustomerName() {
		return ParentCustomerName;
	}

	public void setParentCustomerName(String parentCustomerName) {
		ParentCustomerName = parentCustomerName;
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

	public ArrayList<ResourceVO> getResources() {
		return resources;
	}

	public void setResources(ArrayList<ResourceVO> resources) {
		this.resources = resources;
	}
	
	public void addResource(ResourceVO resourceVO){
		this.resources.add(resourceVO);
	}

	
	// <Property Name="Version" Type="Edm.String" Nullable="true" />

}