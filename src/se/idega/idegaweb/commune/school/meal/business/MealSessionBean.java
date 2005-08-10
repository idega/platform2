/*
 * $Id: MealSessionBean.java,v 1.1 2005/08/10 23:03:11 laddi Exp $
 * Created on Aug 10, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.business;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.business.CommuneUserBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOSessionBean;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2005/08/10 23:03:11 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class MealSessionBean extends IBOSessionBean  implements MealSession{

	private Object iUserPK;
	private User iUser;

	public User getUser() {
		if (iUser == null && iUserPK != null) {
			try {
				iUser = getUserBusiness().getUser(new Integer(iUserPK.toString()));
			}
			catch (RemoteException re) {
				iUser = null;
			}
		}
		return iUser;
	}

	public void setUser(String userPK) {
		iUserPK = userPK;
		iUser = null;
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