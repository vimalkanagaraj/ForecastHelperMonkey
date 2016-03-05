package com.helpermonkey.vo;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ResourceErrorVO {

	public String IdError;
	
	public String ParentCustomerNameError;
	
    public String OpptyProjectNameError;
    
    public String GradeError;
    
    public String LocationError;
    
    public String RateCardError;
    
    public String CompetencyError;
    
    public String SkillsetError;
    
    public String FTECountError;
    
    public String BillabilityError;
    
    public String RequiredByDateError;
    
    public String BillingStartDateError;
    
    public String BillingEndDateError;
    
    public String ConfidenceError;
    
    public String SOUniqueNumberError;
    
    public String AssociateIdError;
    
    public String AssociateNameError;
    
    public String ResourceStatusError;
    
    public String RemarksError;
    
    public String generalError;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (generalError != null) {
			builder.append("GeneralError=");
			builder.append(generalError);
			builder.append(", ");
		}
		if (IdError != null) {
			builder.append("IdError=");
			builder.append(IdError);
			builder.append(", ");
		}
		if (ParentCustomerNameError != null) {
			builder.append("ParentCustomerNameError=");
			builder.append(ParentCustomerNameError);
			builder.append(", ");
		}
		if (OpptyProjectNameError != null) {
			builder.append("OpptyProjectNameError=");
			builder.append(OpptyProjectNameError);
			builder.append(", ");
		}
		if (GradeError != null) {
			builder.append("GradeError=");
			builder.append(GradeError);
			builder.append(", ");
		}
		if (LocationError != null) {
			builder.append("LocationError=");
			builder.append(LocationError);
			builder.append(", ");
		}
		if (RateCardError != null) {
			builder.append("RateCardError=");
			builder.append(RateCardError);
			builder.append(", ");
		}
		if (CompetencyError != null) {
			builder.append("CompetencyError=");
			builder.append(CompetencyError);
			builder.append(", ");
		}
		if (SkillsetError != null) {
			builder.append("SkillsetError=");
			builder.append(SkillsetError);
			builder.append(", ");
		}
		if (FTECountError != null) {
			builder.append("FTECountError=");
			builder.append(FTECountError);
			builder.append(", ");
		}
		if (BillabilityError != null) {
			builder.append("BillabilityError=");
			builder.append(BillabilityError);
			builder.append(", ");
		}
		if (RequiredByDateError != null) {
			builder.append("RequiredByDateError=");
			builder.append(RequiredByDateError);
			builder.append(", ");
		}
		if (BillingStartDateError != null) {
			builder.append("BillingStartDateError=");
			builder.append(BillingStartDateError);
			builder.append(", ");
		}
		if (BillingEndDateError != null) {
			builder.append("BillingEndDateError=");
			builder.append(BillingEndDateError);
			builder.append(", ");
		}
		if (ConfidenceError != null) {
			builder.append("ConfidenceError=");
			builder.append(ConfidenceError);
			builder.append(", ");
		}
		if (SOUniqueNumberError != null) {
			builder.append("SOUniqueNumberError=");
			builder.append(SOUniqueNumberError);
			builder.append(", ");
		}
		if (AssociateIdError != null) {
			builder.append("AssociateIdError=");
			builder.append(AssociateIdError);
			builder.append(", ");
		}
		if (AssociateNameError != null) {
			builder.append("AssociateNameError=");
			builder.append(AssociateNameError);
			builder.append(", ");
		}
		if (ResourceStatusError != null) {
			builder.append("ResourceStatusError=");
			builder.append(ResourceStatusError);
			builder.append(", ");
		}
		if (RemarksError != null) {
			builder.append("RemarksError=");
			builder.append(RemarksError);
		}
		return builder.toString();
	}

	public String getIdError() {
		return IdError;
	}

	public void setIdError(String idError) {
		IdError = idError;
	}

	public String getParentCustomerNameError() {
		return ParentCustomerNameError;
	}

	public void setParentCustomerNameError(String parentCustomerNameError) {
		ParentCustomerNameError = parentCustomerNameError;
	}

	public String getOpptyProjectNameError() {
		return OpptyProjectNameError;
	}

	public void setOpptyProjectNameError(String opptyProjectNameError) {
		OpptyProjectNameError = opptyProjectNameError;
	}

	public String getGradeError() {
		return GradeError;
	}

	public void setGradeError(String gradeError) {
		GradeError = gradeError;
	}

	public String getLocationError() {
		return LocationError;
	}

	public void setLocationError(String locationError) {
		LocationError = locationError;
	}

	public String getRateCardError() {
		return RateCardError;
	}

	public void setRateCardError(String rateCardError) {
		RateCardError = rateCardError;
	}

	public String getCompetencyError() {
		return CompetencyError;
	}

	public void setCompetencyError(String competencyError) {
		CompetencyError = competencyError;
	}

	public String getSkillsetError() {
		return SkillsetError;
	}

	public void setSkillsetError(String skillsetError) {
		SkillsetError = skillsetError;
	}

	public String getFTECountError() {
		return FTECountError;
	}

	public void setFTECountError(String fTECountError) {
		FTECountError = fTECountError;
	}

	public String getBillabilityError() {
		return BillabilityError;
	}

	public void setBillabilityError(String billabilityError) {
		BillabilityError = billabilityError;
	}

	public String getRequiredByDateError() {
		return RequiredByDateError;
	}

	public void setRequiredByDateError(String requiredByDateError) {
		RequiredByDateError = requiredByDateError;
	}

	public String getBillingStartDateError() {
		return BillingStartDateError;
	}

	public void setBillingStartDateError(String billingStartDateError) {
		BillingStartDateError = billingStartDateError;
	}

	public String getBillingEndDateError() {
		return BillingEndDateError;
	}

	public void setBillingEndDateError(String billingEndDateError) {
		BillingEndDateError = billingEndDateError;
	}

	public String getConfidenceError() {
		return ConfidenceError;
	}

	public void setConfidenceError(String confidenceError) {
		ConfidenceError = confidenceError;
	}

	public String getSOUniqueNumberError() {
		return SOUniqueNumberError;
	}

	public void setSOUniqueNumberError(String sOUniqueNumberError) {
		SOUniqueNumberError = sOUniqueNumberError;
	}

	public String getAssociateIdError() {
		return AssociateIdError;
	}

	public void setAssociateIdError(String associateIdError) {
		AssociateIdError = associateIdError;
	}

	public String getAssociateNameError() {
		return AssociateNameError;
	}

	public void setAssociateNameError(String associateNameError) {
		AssociateNameError = associateNameError;
	}

	public String getResourceStatusError() {
		return ResourceStatusError;
	}

	public void setResourceStatusError(String resourceStatusError) {
		ResourceStatusError = resourceStatusError;
	}

	public String getRemarksError() {
		return RemarksError;
	}

	public void setRemarksError(String remarksError) {
		RemarksError = remarksError;
	}

	public String getGeneralError() {
		return generalError;
	}

	public void setGeneralError(String generalError) {
		this.generalError = generalError;
	}
	
	
    
    
    
}
