/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.presentation.UserSearcher;

import java.rmi.RemoteException;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.Parameter;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.presentation.UserChooserBrowser;

/**
 * @author palli
 */
public class SelectUser extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "su_submit";
	protected static final String PARAMETER_OLD_USER_ID = "cashier_old_user_id";
	
	private final static String LABEL_SELECTED_USER = "isi_acc_se_selected_user";
	
	/**
	 * 
	 */
	public SelectUser() {
		super();
	}

	public void main(IWContext iwc) {
		Form f = new Form();
		Table t = new Table();
		t.setCellpadding(5);

		//Has a new user been selected?
		String selectedUser = iwc.getParameter(CashierWindow.PARAMETER_USER_ID);
		String oldSelectedUser = iwc.getParameter(PARAMETER_OLD_USER_ID);
		System.out.println("selectedUser = " + selectedUser);
		System.out.println("old selectedUser = " + oldSelectedUser);
			
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER, "Selected user:"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");
		
		int row = 1;
		t.add(labelUser, 1, row);
		t.add(Text.getNonBrakingSpace(), 1, row);
		if (getUser() != null)
			t.add(getUser().getName(), 1, row);
		
		row++;
		t.add(new UserChooserBrowser(CashierWindow.PARAMETER_USER_ID), 1, row++);
		t.add(submit, 1, row);
		
		t.setAlignment(1, 3, "RIGHT");
		
		f.add(t);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		if (selectedUser == null || "".equals(selectedUser)) {
		    if (oldSelectedUser != null && !"".equals(oldSelectedUser)) {
		        f.add(new HiddenInput(CashierWindow.PARAMETER_USER_ID, oldSelectedUser));
		    }
		}
		else {
		    f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		    f.add(new HiddenInput(PARAMETER_OLD_USER_ID, selectedUser));
		}
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
		
		add(f);
	}
	
	// service method
	private UserBusiness getUserBusiness(IWContext iwc) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (RemoteException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
}