/*
 * $Id: StaffUserItem.java,v 1.1 2005/02/01 13:40:20 laddi Exp $
 * Created on 22.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.presentation.item;

import java.rmi.RemoteException;

import com.idega.block.staff.business.StaffUserBusiness;
import com.idega.block.staff.business.StaffUserSession;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.user.data.User;


/**
 * Last modified: 22.11.2004 13:34:38 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public abstract class StaffUserItem extends Block {

	private static final String IW_BUNDLE_IDENTIFIER = "com.idega.block.staff";
	
	private IWBundle iwb;
	private IWResourceBundle iwrb;
	
	private String styleClass;
	
	public void main(IWContext iwc) {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		try {
			handleParameters(iwc);
		}
		catch (RemoteException re) {
			log(re);
		}

		User user = null;
		try {
			user = getSession(iwc).getUser();
		}
		catch (RemoteException re) {
			log(re);
		}
		
		if (user != null) {
			PresentationObject item = null;
			try {
				item = getItem(iwc);
			}
			catch (RemoteException re) {
				log(re);
			}
			if (item != null) {
				if (styleClass != null) {
					item.setStyleClass(styleClass);
				}
				add(item);
			}
		}
	}
	
	private void handleParameters(IWContext iwc) throws RemoteException {
		int userID = -1;
		if (iwc.isParameterSet(StaffUserBusiness.PARAMETER_USER_ID)) {
			try {
				userID = Integer.parseInt(iwc.getParameter(StaffUserBusiness.PARAMETER_USER_ID));
			}
			catch (NumberFormatException e) {
				userID = -1;
			}
		}
		else {
			try {
				userID = iwc.getCurrentUserId();
			}
			catch (NotLoggedOnException nloe) {
				userID = -1;
			}
		}

		if (userID != -1 && userID != getSession(iwc).getUserID()) {
			getSession(iwc).setUserID(userID);
			getSession(iwc).setUser(getBusiness(iwc).getUser(userID));
		}
	}
	
	protected StaffUserBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (StaffUserBusiness) IBOLookup.getServiceInstance(iwac, StaffUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected StaffUserSession getSession(IWUserContext iwuc) {
		try {
			return (StaffUserSession) IBOLookup.getSessionInstance(iwuc, StaffUserSession.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	protected abstract PresentationObject getItem(IWContext iwc) throws RemoteException;
	
	protected IWBundle getBundle() {
		return iwb;
	}
	
	protected IWResourceBundle getResourceBundle() {
		return iwrb;
	}
	
	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}
}