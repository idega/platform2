package com.idega.block.school.business;

import com.idega.business.IBOSessionBean;


public class SchoolContentBusinessBean extends IBOSessionBean implements SchoolContentBusiness {
	public static String PARAMETER_SCHOOL_ID = "pr_schl_id";
	

	public String getParameterSchoolId() {
		return PARAMETER_SCHOOL_ID;	
	}

/*
	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
	}
*/
}
