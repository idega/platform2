/*
 * $Id: UserInfo.java,v 1.1 2005/02/16 11:11:27 laddi Exp $
 * Created on 31.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.presentation;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.LoginTable;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.user.business.UserSession;
import com.idega.user.data.User;
import com.idega.util.PersonalIDFormatter;


/**
 * Last modified: $Date: 2005/02/16 11:11:27 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class UserInfo extends Block {

	private static String BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";

	protected IWBundle iwb;
	protected IWResourceBundle iwrb;
	
	private Image iAccountImage;

	public void main(IWContext iwc) {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		
		try {
			User user = getUserSession(iwc).getUser();
			if (user != null) {
				Table table = new Table(3, 1);
				table.setCellpadding(4);
				table.setWidth(Table.HUNDRED_PERCENT);
				table.setAlignment(3, 1, Table.HORIZONTAL_ALIGN_RIGHT);
				table.setWidth(1, "150");
				
				table.add(PersonalIDFormatter.format(user.getPersonalID(), iwc.getCurrentLocale()), 1, 1);
				
				Text lastName = new Text(user.getLastName());
				lastName.setBold(true);
				
				table.add(lastName, 2, 1);
				table.add(", " + user.getFirstName() + " " + user.getMiddleName(), 2, 1);
				
				LoginTable lt = LoginDBHandler.getUserLogin(((Integer) user.getPrimaryKey()).intValue());
				if (lt != null && iAccountImage != null) {
					table.add(iAccountImage, 3, 1);
				}
				
				add(table);
			}
		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	protected UserSession getUserSession(IWUserContext iwuc) {
		try {
			return (UserSession) IBOLookup.getSessionInstance(iwuc, UserSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}

	public String getBundleIdentifier() {
		return BUNDLE_IDENTIFIER;
	}
	
	public void setAccountImage(Image accountImage) {
		iAccountImage = accountImage;
	}
}