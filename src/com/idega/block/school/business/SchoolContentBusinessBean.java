package com.idega.block.school.business;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.idegaweb.IWApplicationContext;


public class SchoolContentBusinessBean extends IBOSessionBean implements SchoolContentBusiness {
	public static String PARAMETER_SCHOOL_ID = "pr_schl_id";
	

	public String getParameterSchoolId() {
		return PARAMETER_SCHOOL_ID;	
	}
	
	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
	}

}
