package com.idega.block.finance.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class AssessmentBusinessHomeImpl extends IBOHomeImpl implements
		AssessmentBusinessHome {
	public Class getBeanInterfaceClass() {
		return AssessmentBusiness.class;
	}

	public AssessmentBusiness create() throws CreateException {
		return (AssessmentBusiness) super.createIBO();
	}
}