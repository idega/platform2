package com.idega.block.application.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class ApplicationServiceHomeImpl extends IBOHomeImpl implements
		ApplicationServiceHome {
	public Class getBeanInterfaceClass() {
		return ApplicationService.class;
	}

	public ApplicationService create() throws CreateException {
		return (ApplicationService) super.createIBO();
	}
}