/*
 * $Id: AdminMenu.java,v 1.3 2004/09/07 21:37:09 laddi Exp $
 * Created on 7.9.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.member.isi.block.members.presentation;

import com.idega.builder.app.IBApplication;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.user.app.UserApplication;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;


/**
 * 
 *  Last modified: $Date: 2004/09/07 21:37:09 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.3 $
 */
public class AdminMenu extends Block {

	public static final String IW_BUNDLE_IDENTIFIER = "is.idega.idegaweb.member.isi";

	public void main(IWContext iwc) throws Exception {
		User user = null;
		try {
			user = iwc.getCurrentUser();
		}
		catch (NotLoggedOnException nloe) {
			return;
		}
		
		IWBundle iwb = getBundle(iwc);
		
		Table table = new Table();
		table.setCellpadding(0);
		table.setCellspacing(0);
		boolean addSpace = false;
		int column = 1;
		
		IBApplication builder = new IBApplication();
		if (iwc.hasViewPermission(builder)) {
			Image builderIcon = iwb.getImage("/shared/builder.gif");
			
			Link link = new Link(builderIcon);
			link.setWindowToOpen(IBApplication.class);
			link.setOnMouseOverImage(builderIcon, iwb.getImage("/shared/builder_down.gif"));
			table.add(link, column++, 1);
			
			addSpace = true;
		}
		
		if (addSpace) {
			table.setWidth(column++, 1, 1);
		}
		
		UserApplication felix = new UserApplication();
		if (getUserBusiness(iwc).hasTopNodes(user, iwc)) {
			Image felixIcon = iwb.getImage("/shared/felix.gif");
			
			Link link = new Link(felixIcon);
			link.setWindowToOpen(UserApplication.class);
			link.setOnMouseOverImage(felixIcon, iwb.getImage("/shared/felix_down.gif"));
			table.add(link, column++, 1);
		}
		
		add(table);
	}
	
	public UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (IBOLookupException ile) {
			throw new IBORuntimeException(ile);
		}
	}

	public String getBundleIdentifier() {
		return IW_BUNDLE_IDENTIFIER;
	}
}