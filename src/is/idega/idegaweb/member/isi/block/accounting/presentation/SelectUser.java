/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.presentation.UserSearcher;

import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.Parameter;

/**
 * @author palli
 */
public class SelectUser extends CashierSubWindowTemplate {
	private final static String USER_CHOOSER_NAME = "se_name";
	
	private final static String LABEL_SELECT_USER = "isi_acc_se_select_user";
	
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

		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECT_USER, "Select user"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		int row = 1;
		t.add(labelUser, 1, row++);
		t.add(getSearchItem(iwc, USER_CHOOSER_NAME), 1, row);
		
		f.add(t);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
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
//		searcher.setHeaderFontStyleName(getStyleName(STYLENAME_SMALL_HEADER));
//		searcher.setButtonStyleName(getStyleName(STYLENAME_INTERFACE_BUTTON));
		searcher.setPersonalIDLength(10);
		searcher.setFirstNameLength(20);
		searcher.setLastNameLength(20);
		if (iwc.isParameterSet(name)) {
			searcher.maintainParameter(new Parameter(name, iwc.getParameter(name)));
		}
		table.add(searcher, 1, 1);
		
		return table;
	}
}