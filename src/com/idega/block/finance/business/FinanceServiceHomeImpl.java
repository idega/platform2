package com.idega.block.finance.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class FinanceServiceHomeImpl extends IBOHomeImpl implements
		FinanceServiceHome {
	public Class getBeanInterfaceClass() {
		return FinanceService.class;
	}

	public FinanceService create() throws CreateException {
		return (FinanceService) super.createIBO();
	}
}