package com.stee.spfcore.service.hr.impl;

import java.util.List;
import java.util.Set;

import com.stee.spfcore.service.hr.impl.input.CodeInfo;

public class RecordResult {

	private boolean isSuccess = true;
	private Set<CodeInfo> nonMatchingCodes;
	private List<RecordError> recordErrors;
	private boolean serviceTypeChanged = false;
	
	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public Set<CodeInfo> getNonMatchingCodes() {
		return nonMatchingCodes;
	}

	public void setNonMatchingCodes(Set<CodeInfo> nonMatchingCodes) {
		this.nonMatchingCodes = nonMatchingCodes;
	}

	public List<RecordError> getRecordErrors() {
		return recordErrors;
	}

	public void setRecordErrors(List<RecordError> recordErrors) {
		this.recordErrors = recordErrors;
	}

	public boolean isServiceTypeChanged() {
		return serviceTypeChanged;
	}

	public void setServiceTypeChanged(boolean serviceTypeChanged) {
		this.serviceTypeChanged = serviceTypeChanged;
	}

}
