/*
 * $Id: UserComments.java,v 1.1 2005/02/16 11:11:27 laddi Exp $
 * Created on 31.1.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.block.family.presentation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.presentation.Layer;
import com.idega.presentation.Table;
import com.idega.presentation.text.Break;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextArea;
import com.idega.user.business.UserBusiness;
import com.idega.user.business.UserSession;
import com.idega.user.data.User;
import com.idega.user.data.UserComment;


/**
 * Last modified: $Date: 2005/02/16 11:11:27 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.1 $
 */
public class UserComments extends Block {

	private static String BUNDLE_IDENTIFIER = "is.idega.idegaweb.member";
	
	private static final String PARAMETER_COMMENT = "comment";

	protected IWBundle iwb;
	protected IWResourceBundle iwrb;

	public void main(IWContext iwc) {
		iwb = getBundle(iwc);
		iwrb = getResourceBundle(iwc);
		parse(iwc);
		
		try {
			User user = getUserSession(iwc).getUser();
			if (user != null) {
				Form form = new Form();
				
				Table table = new Table(1, 3);
				table.setCellpadding(4);
				table.setWidth(Table.HUNDRED_PERCENT);
				table.setAlignment(1, 3, Table.HORIZONTAL_ALIGN_RIGHT);
				form.add(table);
				
				Layer layer = new Layer(Layer.DIV);
				layer.setWidth(Table.HUNDRED_PERCENT);
				layer.setHeight(170);
				layer.setOverflow("scroll");
				layer.setPadding(4);
				table.add(layer, 1, 1);
				
				try {
					Collection comments = getUserBusiness(iwc).getUserComments(user);
					Iterator iter = comments.iterator();
					while (iter.hasNext()) {
						UserComment comment = (UserComment) iter.next();
						layer.add(comment.getComment());
						if (iter.hasNext()) {
							layer.add(new Break(2));
						}
					}
				}
				catch (FinderException fe) {
					//No comments found...
				}
				
				table.add(iwrb.getLocalizedString("new_comment", "New comment") + ":", 1, 2);
				table.add(new Break(), 1, 2);
				TextArea area = new TextArea(PARAMETER_COMMENT);
				area.setWidth(Table.HUNDRED_PERCENT);
				area.setRows(10);
				table.add(area, 1, 2);
				
				SubmitButton button = new SubmitButton(iwrb.getLocalizedString("save", "Save"));
				table.add(button, 1, 3);
				
				add(form);
			}
		}
		catch (RemoteException re) {
			log(re);
		}
	}
	
	private void parse(IWContext iwc) {
		if (iwc.isParameterSet(PARAMETER_COMMENT)) {
			try {
				getUserBusiness(iwc).storeUserComment(getUserSession(iwc).getUser(), iwc.getParameter(PARAMETER_COMMENT), iwc.getCurrentUser());
			}
			catch (RemoteException re) {
				log(re);
			}
		}
	}
	
	protected UserBusiness getUserBusiness(IWApplicationContext iwac) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwac, UserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
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
}