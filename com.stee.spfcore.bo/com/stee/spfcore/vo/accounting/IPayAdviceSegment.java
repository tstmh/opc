package com.stee.spfcore.vo.accounting;

import java.util.List;

public interface IPayAdviceSegment {
	
	public List<PayAdviceEntity> getEntities();
	public String print();
	public boolean validateData();
}
