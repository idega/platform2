/*
 * Created on 1.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.staff.business;

import com.idega.business.IBOSession;
import com.idega.user.data.User;

/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface StaffUserSession extends IBOSession {
	/**
	 * @see com.idega.block.staff.business.StaffUserSessionBean#setUser
	 */
	public void setUser(User user) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserSessionBean#getUser
	 */
	public User getUser() throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserSessionBean#setUserID
	 */
	public void setUserID(int userID) throws java.rmi.RemoteException;

	/**
	 * @see com.idega.block.staff.business.StaffUserSessionBean#getUserID
	 */
	public int getUserID() throws java.rmi.RemoteException;

}
