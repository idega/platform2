/*
 * $Id: MealChildren.java,v 1.1 2005/08/12 08:54:40 gimmi Exp $
 * Created on Aug 11, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.school.meal.presentation;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.presentation.CitizenChildren;
import se.idega.idegaweb.commune.school.meal.business.MealSession;
import se.idega.idegaweb.commune.school.presentation.SchoolChildren;
import com.idega.business.IBOLookup;
import com.idega.business.IBORuntimeException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.presentation.IWContext;


public class MealChildren extends SchoolChildren implements IWPageEventListener{

	public boolean actionPerformed(IWContext iwc) throws IWException {
		try {
			MealSession session = getMealSession(iwc);
			
			if (iwc.isParameterSet(CitizenChildren.prmChildId)) {
				session.setUser(iwc.getParameter(CitizenChildren.prmChildId));
			}
			else if (iwc.isParameterSet(CitizenChildren.prmChildUniqueId)) {
				session.setUserUniqueID(iwc.getParameter(CitizenChildren.prmChildUniqueId));
			}
			
			return true;
		}
		catch (RemoteException re) {
			return false;
		}
	}
	
	private MealSession getMealSession(IWContext iwc) {
		try {
			return (MealSession) IBOLookup.getSessionInstance(iwc, MealSession.class);	
		}
		catch (RemoteException re) {
			throw new IBORuntimeException(re);
		}
	}

	protected Class getEventListener() {
		return this.getClass();
	}
}
