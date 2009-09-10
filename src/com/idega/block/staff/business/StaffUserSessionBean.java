/*
 * $Id: StaffUserSessionBean.java,v 1.1.2.1 2007/01/12 19:32:48 idegaweb Exp $
 * Created on 16.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.business;

import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;


/**
 * Last modified: 16.11.2004 12:03:32 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1.2.1 $
 */
public class StaffUserSessionBean extends IBOSessionBean implements StaffUserSession{

	private User user;
	private int userID = -1;
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public int getUserID() {
		return this.userID;
	}
}