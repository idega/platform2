package com.idega.block.staff.presentation;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2000-2004 idega.is All Rights Reserved
 * Company:      idega
  *@author <a href="mailto:laddi@idega.is">Thorhallur "Laddi" Helgason</a>
 * @version 2.0
 */

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.staff.business.StaffBusiness;
import com.idega.block.staff.business.StaffUserBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.core.contact.data.Email;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.block.presentation.Builderaware;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.Group;
import com.idega.user.data.User;

public class StaffViewer extends Block implements Builderaware {

	private final static String IW_BUNDLE_IDENTIFIER = "com.idega.block.staff";

	private IWBundle iwb;
	private IWResourceBundle iwrb;
	
	private Group iGroup;
	private ICPage iPage;

	public void main(IWContext iwc) throws Exception {
		iwb = iwc.getIWMainApplication().getBundle(IW_CORE_BUNDLE_IDENTIFIER);
		iwrb = getResourceBundle(iwc);
		
		if (iGroup != null) {
			add(getStaff(iwc));
		}
		else {
			add("No parent group selected");
		}
	}
	
	private Table getStaff(IWContext iwc) {
		System.out.println("Parent group: " + iGroup.getName());
		Collection groups = null;
		try {
			groups = getBusiness(iwc).getGroups(iGroup);
		}
		catch (RemoteException re) {
			log(re);
		}
		
		Table table = new Table();
		int row = 1;
		
		if (groups != null) {
			Iterator iter = groups.iterator();
			while (iter.hasNext()) {
				Group group = (Group) iter.next();
				System.out.println(group.getName());
				Collection users = null;
				try {
					users = getBusiness(iwc).getUsersInGroup(group);
				}
				catch (RemoteException re) {
					log(re);
				}
				
				if (users != null && users.size() > 0) {
					table.add(group.getName(), 1, row++);
					
					Iterator iterator = users.iterator();
					while (iterator.hasNext()) {
						User user = (User) iterator.next();
						String title = null;
						try {
							title = getBusiness(iwc).getUserTitle(user, iwc.getCurrentLocale());
						}
						catch (RemoteException re) {
							log(re);
						}
						Email mail = null;
						try {
							mail = getBusiness(iwc).getUserMail(user);
						}
						catch (RemoteException re) {
							log(re);
						}
						
						table.add(getStaffUser(user), 1, row);
						if (title != null) {
							table.add(title, 2, row);
						}
						if (mail != null && mail.getEmailAddress() != null) {
							table.add(getEmailLink(mail.getEmailAddress()), 3, row);
						}
						row++;
					}
				}
			}
		}
		
		return table;
	}
	
	private PresentationObject getStaffUser(User user) {
		if (iPage != null) {
			Link link = new Link(user.getName());
			link.addParameter(StaffBusiness.PARAMETER_USER_ID, user.getPrimaryKey().toString());
			link.setPage(iPage);

			return link;
		}
		else {
			return new Text(user.getName());
		}
	}

	private Link getEmailLink(String email) {
		Link link = new Link(email);
		link.setURL("mailto:" + email);

		return link;
	}

	private StaffUserBusiness getBusiness(IWApplicationContext iwac) {
		try {
			return (StaffUserBusiness) IBOLookup.getServiceInstance(iwac, StaffUserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
	
	/**
	 * Sets the root group to find Users from, is not included itself.
	 * @param group The group to set.
	 */
	public void setGroup(Group group) {
		iGroup = group;
	}
	
	public void setPage(ICPage page) {
		iPage = page;
	}
}