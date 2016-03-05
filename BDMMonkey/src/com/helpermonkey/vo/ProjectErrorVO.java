package com.helpermonkey.vo;

/**
 * @author Vimal (Vimalasekar Rajendran; 131807)
 *
 */
public class ProjectErrorVO {

	public String idError;

	public String OpptyProjectNameError;

	public String ESAProjectIdError;

	public String ParentCustomerNameError;

	public String ProjectTypeError;

	public String RemarksError;

	public String StatusValueError;
	
	public String generalError;
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (idError != null) {
			builder.append("idError=");
			builder.append(idError);
			builder.append(", ");
		}
		if (OpptyProjectNameError != null) {
			builder.append("OpptyProjectNameError=");
			builder.append(OpptyProjectNameError);
			builder.append(", ");
		}
		if (ESAProjectIdError != null) {
			builder.append("ESAProjectIdError=");
			builder.append(ESAProjectIdError);
			builder.append(", ");
		}
		if (ParentCustomerNameError != null) {
			builder.append("ParentCustomerNameError=");
			builder.append(ParentCustomerNameError);
			builder.append(", ");
		}
		if (ProjectTypeError != null) {
			builder.append("ProjectTypeError=");
			builder.append(ProjectTypeError);
			builder.append(", ");
		}
		if (RemarksError != null) {
			builder.append("RemarksError=");
			builder.append(RemarksError);
			builder.append(", ");
		}
		if (StatusValueError != null) {
			builder.append("StatusValueError=");
			builder.append(StatusValueError);
			builder.append(", ");
		}
		if (generalError != null) {
			builder.append("generalError=");
			builder.append(generalError);
		}
		return builder.toString();
	}

	public String getIdError() {
		return idError;
	}

	public void setIdError(String idError) {
		this.idError = idError;
	}

	public String getOpptyProjectNameError() {
		return OpptyProjectNameError;
	}

	public void setOpptyProjectNameError(String opptyProjectNameError) {
		OpptyProjectNameError = opptyProjectNameError;
	}

	public String getESAProjectIdError() {
		return ESAProjectIdError;
	}

	public void setESAProjectIdError(String eSAProjectIdError) {
		ESAProjectIdError = eSAProjectIdError;
	}

	public String getParentCustomerNameError() {
		return ParentCustomerNameError;
	}

	public void setParentCustomerNameError(String parentCustomerNameError) {
		ParentCustomerNameError = parentCustomerNameError;
	}

	public String getProjectTypeError() {
		return ProjectTypeError;
	}

	public void setProjectTypeError(String projectTypeError) {
		ProjectTypeError = projectTypeError;
	}

	public String getRemarksError() {
		return RemarksError;
	}

	public void setRemarksError(String remarksError) {
		RemarksError = remarksError;
	}

	public String getStatusValueError() {
		return StatusValueError;
	}

	public void setStatusValueError(String statusValueError) {
		StatusValueError = statusValueError;
	}

	public String getGeneralError() {
		return generalError;
	}

	public void setGeneralError(String generalError) {
		this.generalError = generalError;
	}
	
}