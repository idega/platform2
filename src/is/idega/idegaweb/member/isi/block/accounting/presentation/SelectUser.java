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
import com.idega.user.data.User;

/**
 * @author palli
 */
public class SelectUser extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "su_submit";
	
	private final static String USER_CHOOSER_NAME = "su_name";
	
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
		String userPrm = UserSearcher.getUniqueUserParameterName(USER_CHOOSER_NAME);
		String selectedUser = iwc.getParameter(userPrm);
			
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
		t.add(getSearchItem(iwc, USER_CHOOSER_NAME), 1, row++);
		t.add(submit, 1, row);
		
		t.setAlignment(1, 3, "RIGHT");
		
		f.add(t);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		if (selectedUser != null) 
			f.add(new HiddenInput(CashierWindow.PARAMETER_USER_ID, selectedUser));
		else
			f.maintainParameter(CashierWindow.PARAMETER_USER_ID);

		add(f);
	}
	
	public PresentationObject getSearchItem(IWContext iwc, String name) {
		Table table = new Table();
		UserSearcher searcher = new UserSearcher();
		searcher.setShowMiddleNameInSearch(false);
		searcher.setOwnFormContainer(false);
		searcher.setUniqueIdentifier(name);
		searcher.setSkipResultsForOneFound(false);
		searcher.setPersonalIDLength(10);
		searcher.setFirstNameLength(20);
		searcher.setLastNameLength(20);
		String action = iwc.getParameter(CashierWindow.ACTION);
		String group = iwc.getParameter(CashierWindow.PARAMETER_GROUP_ID);
//		String division = iwc.getParameter(CashierWindow.)
		String user = iwc.getParameter(CashierWindow.PARAMETER_USER_ID);
		if (action != null)
			searcher.maintainParameter(new Parameter(CashierWindow.ACTION, action));
		if (group != null)
			searcher.maintainParameter(new Parameter(CashierWindow.PARAMETER_GROUP_ID, group));
		if (user != null)
			searcher.maintainParameter(new Parameter(CashierWindow.PARAMETER_USER_ID, user));
		if (iwc.isParameterSet(name)) {
			searcher.maintainParameter(new Parameter(name, iwc.getParameter(name)));
		}
		String prmName = UserSearcher.getUniqueUserParameterName(name);
		System.out.println("prmName = " + prmName);
		if (iwc.isParameterSet(prmName)) {
			System.out.println("parameter is set");
			System.out.println("value = " + iwc.getParameter(prmName));
			searcher.maintainParameter(new Parameter(prmName, iwc.getParameter(prmName)));
		}
		table.add(searcher, 1, 1);
		
		return table;
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