/*
 * $Id: StaffUserTitle.java,v 1.1 2005/02/01 13:40:20 laddi Exp $
 * Created on 22.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.presentation.item;

import java.rmi.RemoteException;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;


/**
 * Last modified: 22.11.2004 13:49:50 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class StaffUserTitle extends StaffUserItem {

	/* (non-Javadoc)
	 * @see com.idega.block.staff.presentation.item.StaffUserItem#getItem(com.idega.presentation.IWContext)
	 */
	protected PresentationObject getItem(IWContext iwc) throws RemoteException {
		User user = getSession(iwc).getUser();
		String userTitle = getBusiness(iwc).getUserTitle(user, iwc.getCurrentLocale());
		if (userTitle != null) {
			return new Text(userTitle);
		}
		return null;
	}

}
