/*
 * $Id: CitizenFinder.java,v 1.3 2005/10/14 10:37:01 laddi Exp $
 * Created on Oct 14, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.presentation;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;


/**
 * Last modified: $Date: 2005/10/14 10:37:01 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class CitizenFinder extends CommuneUserFinder implements IWPageEventListener {

	private static final String PARAMETER_USER_PK = "cf_user_pk";
	private static final String PARAMETER_USER_UNIQUE_ID = "cf_user_unique_id";
	
	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			if (iwc.isParameterSet(PARAMETER_USER_UNIQUE_ID)) {
				getUserSession(iwc).setUser(getUserBusiness(iwc).getUserByUniqueId(iwc.getParameter(PARAMETER_USER_UNIQUE_ID)));
				return true;
			}
			else if (iwc.isParameterSet(PARAMETER_USER_PK)) {
				getUserSession(iwc).setUser(getUserBusiness(iwc).getUser(new Integer(iwc.getParameter(PARAMETER_USER_PK))));
				return true;
			}
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
		}
		return false;
	}

	protected Collection getUsers(IWContext iwc, String searchString) throws RemoteException {
		try {
			UserHome home = (UserHome) IDOLookup.getHome(User.class);
			return home.findUsersBySearchCondition(searchString, false);
		}
		catch (FinderException fe) {
			fe.printStackTrace();
			return new ArrayList();
		}
	}
	
	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#addUser(com.idega.presentation.IWContext, com.idega.user.data.User)
	 */
	public boolean addUser(IWContext iwc, User user) {
		return true;
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getParameterName(com.idega.presentation.IWContext)
	 */
	public String getParameterName(IWContext iwc) {
		return PARAMETER_USER_PK;
	}

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getParameterUniqueName(com.idega.presentation.IWContext)
	 */
	public String getParameterUniqueName(IWContext iwc) {
		return PARAMETER_USER_UNIQUE_ID;
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getSearchSubmitDisplay()
	 */
	public String getSearchSubmitDisplay() {
		return localize("user.search","Search");
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getNoUserFoundString()
	 */
	public String getNoUserFoundString() {
		return localize("user.no_user_found","No user found");
	}

	/**
	 * @see se.idega.idegaweb.commune.presentation.CommuneUserFinder#getFoundUsersString()
	 */
	public String getFoundUsersString() {
		return localize("user.found_users","Found users");
	}

	public Class getEventListener() {
		return CitizenFinder.class;
	}

	public String getSubmitDisplay() {
		return localize("user.find_user", "Find user");
	}
}