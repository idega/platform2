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
import com.idega.user.presentation.UserChooser;

/**
 * @author palli
 */
public class UserPayment extends CashierSubWindowTemplate {
	private final static String USER_CHOOSER_NAME = "up_user_chooser_name";
	
	private final static String LABEL_SELECT_USER = "isi_acc_up_select_user";
	
	/**
	 * 
	 */
	public UserPayment() {
		super();
	}

	public void main(IWContext iwc) {
		Form f = new Form();
		Table t = new Table();
		t.setCellpadding(5);

		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECT_USER, "Select user"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		UserChooser chooser = new UserChooser(USER_CHOOSER_NAME);
		
		int row = 1;
		t.add(labelUser,1,row++);
		t.add(chooser,1,row);
		
		f.add(t);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		
		add(f);
	}
}