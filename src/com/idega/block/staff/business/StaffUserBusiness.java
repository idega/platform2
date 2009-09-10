/*
 * Created on 1.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.staff.business;

import java.sql.Date;
import java.util.Collection;
import java.util.Locale;

import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface StaffUserBusiness extends UserBusiness {
	
	public static final String PARAMETER_USER_ID = "st_user_id";
	
	/**
	 * @see com.idega.block.staff.business.StaffUserBusinessBean#getGroups
	 */
	public Collection getGroups(Group parentGroup)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserBusinessBean#getUserEducation
	 */
	public String getUserEducation(User user, Locale locale)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserBusinessBean#getUserTitle
	 */
	public String getUserTitle(User user, Locale locale)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserBusinessBean#getUserArea
	 */
	public String getUserArea(User user, Locale locale)
			throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserBusinessBean#getBeganWork
	 */
	public Date getBeganWork(User user) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserBusinessBean#storeStaffUser
	 */
	public void storeStaffUser(User user, String education, String title,
			String area, Date beganWork, Locale locale)
			throws java.rmi.RemoteException;

}
