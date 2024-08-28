package com.stee.spfcore.vo.accounting;

public interface IPayAdvice {

	public boolean validatePayAdvice();
	public String getFormatedData();
	public void processRecord();
}
