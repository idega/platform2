/*
 * Created on 1.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.staff.presentation.item;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.util.Age;

/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StaffUserAge extends StaffUserItem {

	/* (non-Javadoc)
	 * @see com.idega.block.staff.presentation.item.StaffUserItem#getItem(com.idega.presentation.IWContext)
	 */
	protected PresentationObject getItem(IWContext iwc) throws RemoteException {
		User user = getSession(iwc).getUser();
		if (user.getDateOfBirth() != null) {
			Age age = new Age(user.getDateOfBirth());
			return new Text(String.valueOf(age.getYears()));
		}
		return null;
	}
}