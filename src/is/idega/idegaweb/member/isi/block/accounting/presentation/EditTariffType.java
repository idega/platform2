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
	protected static final String ACTION_DELETE = "ett_delete";
	
	protected static final String LABEL_NAME = "isi_acc_ett_name";

	/**
	 *  
	 */
	public EditTariffType() {
		super();
	}

	private void saveTariffType(IWContext iwc) {
		String name = iwc.getParameter(LABEL_NAME);

		try {
			getAccountingBusiness(iwc).insertTariffType(null,name,null,getClub());
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
		Table inputTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);

		int row = 1;
		Text labelName = new Text(iwrb.getLocalizedString(LABEL_NAME, "Name"));
		labelName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		inputTable.add(labelName, 1, row++);
		
		TextInput nameInput = new TextInput(LABEL_NAME);
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		inputTable.add(nameInput, 1, row);
		inputTable.add(submit, 2, row);
		
		row = 1;
		t.add(labelName, 2, row++);

		Collection col = null;
		try {
			System.out.println("EditTariffType.getClub() = " + getClub());
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
				t.add(type.getName(), 2, row);
				row++;
			}
			
			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			t.add(delete, 10, row);
		}

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);

		f.add(inputTable);
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