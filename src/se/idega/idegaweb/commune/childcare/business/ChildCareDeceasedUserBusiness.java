/*
 * $Id: ChildCareDeceasedUserBusiness.java,v 1.1 2004/10/05 18:44:43 thomas Exp $
 * Created on Oct 5, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.business;

import java.rmi.RemoteException;
import java.util.Date;
import se.idega.idegaweb.commune.user.business.DeceasedUserBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWApplicationContext;


/**
 * 
 *  Last modified: $Date: 2004/10/05 18:44:43 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.1 $
 */
public class ChildCareDeceasedUserBusiness implements DeceasedUserBusiness {

	/* (non-Javadoc)
	 * @see se.idega.idegaweb.commune.user.business.DeceisedUser#setUserAsDeceased(java.lang.Integer, java.util.Date)
	 */
	public boolean setUserAsDeceased(Integer userID, Date deceasedDate, IWApplicationContext iwac) {
		ChildCareBusiness business;
		try {
			business = (ChildCareBusiness) IBOLookup.getServiceInstance(iwac, ChildCareBusiness.class);
			return business.setUserAsDeceased(userID, deceasedDate);
		}
		catch (IBOLookupException e) {
			// empty
		}
		catch (RemoteException e) {
			// empty
		}
		return false;
	}
}
