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
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author palli
 */
public class EditTariffType extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "ett_submit";
	protected static final String ACTION_DELETE = "ett_delete";
	
//	protected static final String LABEL_CLUB = "isi_acc_ett_club";
	protected static final String LABEL_NAME = "isi_acc_ett_name";
	protected static final String LABEL_LIST_NAMES = "isi_acc_ett_list_names";
	protected static final String LABEL_DELETE = "isi_acc_ett_delete";
		
	/**
	 *  
	 */
	public EditTariffType() {
		super();
	}

	private boolean saveTariffType(IWContext iwc) {
		String name = iwc.getParameter(LABEL_NAME);

		if (name != null && !"".equals(name)) {
			try {
				getAccountingBusiness(iwc).insertTariffType(null,name,null,getClub());
			
				return true;
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}

	private void deleteTariffType(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);
		
		try {
			getAccountingBusiness(iwc).deleteTariffType(delete);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void main(IWContext iwc) {
		Form f = new Form();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			if (!saveTariffType(iwc)) {
				Text error = new Text(iwrb.getLocalizedString(ERROR_COULD_NOT_SAVE, "Could not save tariff type"));
				error.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);
				
				f.add(error);
			}
		}
		else if (iwc.isParameterSet(ACTION_DELETE)) {
			deleteTariffType(iwc);
		}

		Table t = new Table();
		Table inputTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);

		int row = 1;
		Text labelName = new Text(iwrb.getLocalizedString(LABEL_NAME, "Name"));
		labelName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelListName = new Text(iwrb.getLocalizedString(LABEL_LIST_NAMES, "Names"));
		labelListName.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		inputTable.add(labelName, 1, row++);
		
		TextInput nameInput = new TextInput(LABEL_NAME);
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		inputTable.add(nameInput, 1, row);
		inputTable.add(submit, 2, row);
		
		row = 1;
		t.add(labelListName, 2, row++);
		
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
				CheckBox delete = new CheckBox(LABEL_DELETE, type.getPrimaryKey().toString());
				t.add(delete, 1, row);
				t.add(type.getName(), 2, row);
				row++;
			}
			
			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			delete.setToEnableWhenChecked(LABEL_DELETE);
			t.add(delete, 2, row);
			t.setAlignment(2, row, "RIGHT");
		}

		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		
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