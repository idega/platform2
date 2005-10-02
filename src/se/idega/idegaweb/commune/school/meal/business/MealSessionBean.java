/*
 * $Id: MealSessionBean.java,v 1.3 2005/10/02 13:44:24 laddi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.block.school.data.School;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/10/02 13:44:24 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class MealSessionBean extends IBOSessionBean  implements MealSession{

	private Object iUserPK;
	private User iUser;
	private String iUserUniqueID;
	
	private School iSchool;
	
	public User getUser() {
		try {
			if (iUser == null && iUserPK != null) {
				iUser = getUserBusiness().getUser(new Integer(iUserPK.toString()));
			}
			else if (iUser == null && iUserUniqueID != null) {
				try {
					iUser = getUserBusiness().getUserByUniqueId(iUserUniqueID);
				}
				catch (FinderException fe) {
					fe.printStackTrace();
					iUser = null;
				}
			}
		}
		catch (RemoteException re) {
			iUser = null;
		}
		return iUser;
	}

	public void setUser(Object userPK) {
		iUserPK = userPK;
		iUserUniqueID = null;
		iUser = null;
	}
	
	public void setUserUniqueID(String uniqueID) {
		iUserUniqueID = uniqueID;
		iUserPK = null;
		iUser = null;
	}

	public School getSchool() throws RemoteException {
		if (getUserContext().isLoggedOn()) {
			User user = getUserContext().getCurrentUser();
			Object userID = user.getPrimaryKey();
			
			if (iUserPK != null && iUserPK.equals(userID)) {
				if (iSchool != null) {
					return iSchool;
				}
				else {
					return getSchoolIDFromUser(user);
				}
			}
			else {
				iUserPK = userID;
				return getSchoolIDFromUser(user);
			}
		}
		else {
			return null;	
		}
	}
	
	private School getSchoolIDFromUser(User user) throws RemoteException {
		if (user != null) {
			try {
				School school = getUserBusiness().getFirstManagingMusicSchoolForUser(user);
				if (school != null) {
					iSchool = school;
				}
			}
			catch (FinderException fe) {
				//No school found for user
				log(fe);
			}
		}
		return iSchool;
	}

	private CommuneUserBusiness getUserBusiness() {
		try {
			return (CommuneUserBusiness) IBOLookup.getServiceInstance(this.getIWApplicationContext(), CommuneUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}
}