/*
 * $Id: StaffUserBeganWork.java,v 1.1 2005/02/01 13:40:20 laddi Exp $
 * Created on 22.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.staff.presentation.item;

import java.rmi.RemoteException;
import java.sql.Date;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * Last modified: 22.11.2004 13:57:43 by laddi
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class StaffUserBeganWork extends StaffUserItem {

	/* (non-Javadoc)
	 * @see com.idega.block.staff.presentation.item.StaffUserItem#getItem(com.idega.presentation.IWContext)
	 */
	protected PresentationObject getItem(IWContext iwc) throws RemoteException {
		User user = getSession(iwc).getUser();
		Date beganWork = getBusiness(iwc).getBeganWork(user);
		if (beganWork != null) {
			IWTimestamp stamp = new IWTimestamp(beganWork);
			return new Text(stamp.getLocaleDate(iwc.getCurrentLocale()));
		}
		return null;
	}
}