package com.helpermonkey.vo;

import com.helpermonkey.common.MonkeyConstants;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class MonkeyRolesVO {

	//Type="Edm.Int32" Nullable="false" />
	//This is the resource item id
	public int Id;
	
	public int associateId;
	
	public String associateName;
	
	public String role;
	
	public String vertical;
	
	public String location;
	
	private boolean isOnsite;
	
    public MonkeyRolesVO(){
    	
    }
    
	public boolean equals(Object obj) {
		if(this.Id == ((MonkeyRolesVO) obj).getId()){
			return true;
		}
		
		return super.equals(obj);
	}

	public int getId() {
		return Id;
	}
	public MonkeyRolesVO setId(int id) {
		Id = id;
		return this;
	}

	public int getAssociateId() {
		return associateId;
	}

	public void setAssociateId(int associateId) {
		this.associateId = associateId;
	}

	public String getAssociateName() {
		return associateName;
	}

	public void setAssociateName(String associateName) {
		this.associateName = associateName;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getVertical() {
		return vertical;
	}

	public MonkeyRolesVO setVertical(String vertical) {
		this.vertical = vertical;
		return this;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if(MonkeyConstants.USA_LOCATION_VALUE.equalsIgnoreCase(location) || MonkeyConstants.UK_LOCATION_VALUE.equalsIgnoreCase(location)){
			this.isOnsite = true;
		}
		this.location = location;
	}

	public boolean isOnsite() {
		return isOnsite;
	}
	
	public boolean isRoleDBP(){
		if(MonkeyConstants.ROLE_DBP.equalsIgnoreCase(this.role)){
			return true;
		}
		
		return false;
	}
	
	public boolean isRoleOPS(){
		if(MonkeyConstants.ROLE_OPS.equalsIgnoreCase(this.role)){
			return true;
		}
		
		return false;
	}

}
