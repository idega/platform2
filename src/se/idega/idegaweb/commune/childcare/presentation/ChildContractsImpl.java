/*
 * $Id: ChildContractsImpl.java,v 1.2 2004/10/14 17:32:26 thomas Exp $
 * Created on Oct 8, 2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.childcare.presentation;

import java.rmi.RemoteException;
import se.idega.idegaweb.commune.care.presentation.ChildContracts;
import se.idega.idegaweb.commune.childcare.business.ChildCareSession;
import se.idega.idegaweb.commune.school.business.SchoolConstants;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;


/**
 * 
 *  Last modified: $Date: 2004/10/14 17:32:26 $ by $Author: thomas $
 * 
 * @author <a href="mailto:thomas@idega.com">thomas</a>
 * @version $Revision: 1.2 $
 */
public class ChildContractsImpl implements ChildContracts {
	
	public static ChildCareChildContracts getPresentationObject() {
		return new ChildCareChildContracts();
	}
	
	public PresentationObject getPresentation() {
		return ChildContractsImpl.getPresentationObject();
	}
	
	public Class getWindowClass() {
		return ChildContractsWindow.class;
	}
	
	public String getParameterChildID() {
		return SchoolConstants.PARAMETER_CHILD_ID;
	}
	
	public void storeChildInSession(int childID, IWContext iwc) {
		ChildCareSession sessionBean = getChildCareSession(iwc);
		try {
			sessionBean.setChildID(childID);
		}
		catch (RemoteException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}

	private ChildCareSession getChildCareSession(IWContext iwc) {
		try {
			return (ChildCareSession) IBOLookup.getSessionInstance(iwc, ChildCareSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e.getMessage());
		}
	}


}
