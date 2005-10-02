/*
 * $Id: MealSession.java,v 1.3 2005/10/02 13:44:24 laddi Exp $
 * Created on Sep 30, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import java.rmi.RemoteException;
import com.idega.block.school.data.School;
import com.idega.business.IBOSession;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public interface MealSession extends IBOSession {

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealSessionBean#getUser
	 */
	public User getUser() throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealSessionBean#setUser
	 */
	public void setUser(Object userPK) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealSessionBean#setUserUniqueID
	 */
	public void setUserUniqueID(String uniqueID) throws java.rmi.RemoteException;

	/**
	 * @see se.idega.idegaweb.commune.school.meal.business.MealSessionBean#getSchool
	 */
	public School getSchool() throws RemoteException;
}
