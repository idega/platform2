/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;

/**
 * @author palli
 */
public class Checkout extends CashierSubWindowTemplate {
	private final static String USER_CHOOSER_NAME = "up_user_chooser_name";
	
	private final static String LABEL_SELECTED_USER = "isi_acc_up_select_user";
	
	private final static String ERROR_NO_SELECTED_USER = "isi_acc_no_user_selected";
	
	/**
	 * 
	 */
	public Checkout() {
		super();
	}

	public void main(IWContext iwc) {
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Text notDone = new Text(iwrb.getLocalizedString(NOT_DONE, "Not done. Working on this..."));
		notDone.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

		add(notDone);
		
		/*Form f = new Form();
		Table t = new Table();
		Table inputTable = new Table();
		Table dataTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);
		dataTable.setCellpadding(5);
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER, "Selected user:"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		int row = 1;
		t.add(labelUser, 1, row);
		t.add(Text.getNonBrakingSpace(), 1, row);
		if (getUser() != null) {
			t.add(getUser().getName(), 1, row);
		}
		else {
			t.add(iwrb.getLocalizedString(ERROR_NO_SELECTED_USER, "No user selected. Please select a user in the Select user tab.."), 1, row);
		}
		
		f.add(t);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
		
		add(f);*/
	}
}