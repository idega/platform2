/*
 * $Id: StaffUserBusinessBean.java,v 1.1 2005/02/01 13:40:20 laddi Exp $
 * Created on 16.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.business;

import java.rmi.RemoteException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import com.idega.user.business.UserBusinessBean;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: 16.11.2004 12:03:32 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class StaffUserBusinessBean extends UserBusinessBean implements StaffUserBusiness{

	private static final String EDUCATION = "st_education";
	private static final String TITLE = "st_title";
	private static final String AREA = "st_area";
	private static final String BEGAN_WORK = "st_began_work";
	
	public Collection getGroups(Group parentGroup) {
		try {
			String[] types = { getGroupBusiness().getGroupTypeHome().getGeneralGroupTypeString() };
			Collection groups = getGroupBusiness().getChildGroups(parentGroup, types, true);
			if (groups.size() == 0) {
				groups = new ArrayList();
				groups.add(parentGroup);
			}
			return groups;
		}
		catch (RemoteException re) {
			log (re);
			return null;
		}
	}
	
	public String getUserEducation(User user, Locale locale) {
		return user.getMetaData(EDUCATION + "_" + locale.toString());
	}
	
	public String getUserTitle(User user, Locale locale) {
		return user.getMetaData(TITLE + "_" + locale.toString());
	}
	
	public String getUserArea(User user, Locale locale) {
		return user.getMetaData(AREA + "_" + locale.toString());
	}
	
	public Date getBeganWork(User user) {
		String date = user.getMetaData(BEGAN_WORK);
		if (date != null) {
			IWTimestamp stamp = new IWTimestamp(date);
			return stamp.getDate();
		}
		return null;
	}
	
	public void storeStaffUser(User user, String education, String title, String area, Date beganWork, Locale locale) {
		user.setMetaData(EDUCATION + "_" + locale.toString(), education);
		user.setMetaData(TITLE + "_" + locale.toString(), title);
		user.setMetaData(AREA + "_" + locale.toString(), area);
		
		IWTimestamp stamp = new IWTimestamp(beganWork);
		user.setMetaData(BEGAN_WORK, stamp.toSQLDateString());
		
		user.store();
	}
}