package com.idega.block.school.business;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOSessionBean;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;


public class SchoolContentBusinessBean extends IBOSessionBean implements SchoolContentBusiness {
	public static String PARAMETER_SCHOOL_ID = "pr_schl_id";
	

	public String getParameterSchoolId() {
		return PARAMETER_SCHOOL_ID;	
	}
	
	private SchoolBusiness getSchoolBusiness(IWApplicationContext iwac) throws RemoteException {
		return (SchoolBusiness) IBOLookup.getServiceInstance(iwac, SchoolBusiness.class);
	}

}
