/*
 * $Id: SchoolUserChooserWindow.java,v 1.2 2005/03/07 13:17:20 anna Exp $ Created on
 * 24.2.2005
 * 
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega hf. Use is subject to
 * license terms.
 */
package com.idega.block.school.presentation;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.block.school.business.SchoolBusiness;
import com.idega.block.school.business.SchoolUserBusiness;
import com.idega.block.school.business.SchoolUserBusinessBean;
import com.idega.block.school.data.School;
import com.idega.builder.business.BuilderConstants;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.Form;
import com.idega.user.presentation.UserChooserWindow;

/**
 * Last modified: 24.2.2005 15:06:52 by: anna
 * 
 * @author <a href="mailto:anna@idega.com">anna </a>
 * @version $Revision: 1.2 $
 */
public class SchoolUserChooserWindow extends UserChooserWindow {

	private SchoolBusiness getSchoolBusiness(IWContext iwc) {
		try {
			return (SchoolBusiness) IBOLookup.getServiceInstance(iwc, SchoolBusiness.class);
		}
		catch (IBOLookupException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public School getProvider(IWContext iwc) {
		School _provider = null;
		try {
			_provider = getSchoolBusiness(iwc).getSchool(iwc.getParameter(SchoolUserChooser.PARAMETER_SCHOOL_ID));
		}
		catch (RemoteException ex) {
			ex.printStackTrace();
		}
		return _provider;
	}

	private SchoolUserBusiness getSchoolUserBusiness(IWContext iwc) {
		try {
			return (SchoolUserBusiness) IBOLookup.getServiceInstance(iwc, SchoolUserBusiness.class);
		}
		catch (IBOLookupException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected void init(IWContext iwc) {
		form = new Form();
		form.maintainParameter(SchoolUserChooser.PARAMETER_SCHOOL_ID);
		
		searchString = iwc.getParameter(PARAMETER_SEARCH);
		iwrb = iwc.getIWMainApplication().getBundle(BuilderConstants.STANDARD_IW_BUNDLE_IDENTIFIER).getResourceBundle(iwc);
		showAll = iwc.isParameterSet(PARAMETER_VIEW_ALL);
		if (iwc.isParameterSet(PARAMETER_CURRENT_PAGE)) {
			currentPage = Integer.parseInt(iwc.getParameter(PARAMETER_CURRENT_PAGE));
		}
		try {
			SchoolUserBusiness biz = getSchoolUserBusiness(iwc);
			users = biz.getUsers(getProvider(iwc), SchoolUserBusinessBean.USER_TYPE_HEADMASTER);
			users.addAll(biz.getUsers(getProvider(iwc), SchoolUserBusinessBean.USER_TYPE_ASSISTANT_HEADMASTER));
			users.addAll(biz.getUsers(getProvider(iwc), SchoolUserBusinessBean.USER_TYPE_IB_COORDINATOR));
			users.addAll(biz.getUsers(getProvider(iwc), SchoolUserBusinessBean.USER_TYPE_STUDY_AND_WORK_COUNCEL));
			users.addAll(biz.getUsers(getProvider(iwc), SchoolUserBusinessBean.USER_TYPE_TEACHER));
			users.addAll(biz.getUsers(getProvider(iwc), SchoolUserBusinessBean.USER_TYPE_WEB_ADMIN));
		}
		catch (FinderException ex) {
			ex.printStackTrace();
		}
		catch (RemoteException ex) {
			ex.printStackTrace();
		}
	}
}
