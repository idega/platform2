/*
 * Created on 1.12.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.idega.block.staff.presentation.item;

import java.rmi.RemoteException;

import com.idega.core.contact.data.Email;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Link;
import com.idega.user.business.NoEmailFoundException;
import com.idega.user.data.User;

/**
 * @author laddi
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StaffUserEmail extends StaffUserItem {

	/* (non-Javadoc)
	 * @see com.idega.block.staff.presentation.item.StaffUserItem#getItem(com.idega.presentation.IWContext)
	 */
	protected PresentationObject getItem(IWContext iwc) throws RemoteException {
		User user = getSession(iwc).getUser();
		try {
			Email mail = getBusiness(iwc).getUsersMainEmail(user);
			Link link = new Link(mail.getEmailAddress());
			link.setURL("mailto:" + mail.getEmailAddress());
			return link;
		}
		catch (NoEmailFoundException nefe) {
			//Nothing done
		}
		return null;
	}

}
