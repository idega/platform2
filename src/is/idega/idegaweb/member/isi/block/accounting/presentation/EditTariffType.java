/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffType;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author palli
 */
public class EditTariffType extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "ett_submit";

	protected static final String LABEL_TARIFF_TYPE = "ett_tariff_type";
	protected static final String LABEL_NAME = "ett_name";
	protected static final String LABEL_LOC_KEY = "ett_loc_key";

	/**
	 *  
	 */
	public EditTariffType() {
		super();
	}

	private void saveTariffType(IWContext iwc) {
		String type = iwc.getParameter(LABEL_TARIFF_TYPE);
		String name = iwc.getParameter(LABEL_NAME);
		String locKey = iwc.getParameter(LABEL_LOC_KEY);

		try {
			getAccountingBusiness(iwc).insertTariffType(type,name,locKey,getClub());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			saveTariffType(iwc);
		}

		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();
		Table t = new Table();
		t.setCellpadding(5);

		int row = 1;
		Text labelType = new Text(iwrb.getLocalizedString(LABEL_TARIFF_TYPE, "Tariff type"));
		labelType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelName = new Text(iwrb.getLocalizedString(LABEL_NAME, "Name"));
		labelName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelLocKey = new Text(iwrb.getLocalizedString(LABEL_LOC_KEY, "Localization key"));
		labelLocKey.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		t.add(labelType, 2, row);
		t.add(labelName, 3, row);
		t.add(labelLocKey, 4, row++);

		Collection col = null;
		try {
			if (getClub() != null) {
				col = getAccountingBusiness(iwc).findAllTariffTypeByClub(getClub());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				ClubTariffType type = (ClubTariffType) it.next();
				t.add(type.getTariffType(), 2, row);
				t.add(type.getName(), 3, row);
				t.add(type.getLocalizedKey(), 4, row++);
			}
		}

		row += 6;

		TextInput typeInput = new TextInput(LABEL_TARIFF_TYPE);
		TextInput nameInput = new TextInput(LABEL_NAME);
		TextInput locKeyInput = new TextInput(LABEL_LOC_KEY);
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		t.add(typeInput, 2, row);
		t.add(nameInput, 3, row);
		t.add(locKeyInput, 4, row);
		t.add(submit, 5, row);

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		
		f.add(t);
		add(f);
	}

	private AccountingBusiness getAccountingBusiness(IWApplicationContext iwc) {
		try {
			return (AccountingBusiness) IBOLookup.getServiceInstance(iwc, AccountingBusiness.class);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		return null;
	}
}