/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.util.IWMemberConstants;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.Group;

/**
 * @author palli
 */
public class ClubCreditCardContract extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "etl_submit";
	protected static final String ACTION_DELETE = "etl_delete";

	protected static final String LABEL_DIVISION = "isi_acc_cccc_division";
	protected static final String LABEL_CONTRACT_NUMBER = "isi_acc_cccc_cont_nr";
	protected static final String LABEL_CARD_TYPE = "isi_acc_cccc_card_type";
	protected static final String LABEL_DELETE = "isi_acc_cccc_delete";

	protected static final String ELEMENT_ALL_DIVISIONS = "isi_acc_cccc_all_divisions";
	/**
	 *  
	 */
	public ClubCreditCardContract() {
		super();
	}

	private void saveContract(IWContext iwc) {
		String div = iwc.getParameter(LABEL_DIVISION);
		String number = iwc.getParameter(LABEL_CONTRACT_NUMBER);
		String type = iwc.getParameter(LABEL_CARD_TYPE);

		try {
			getAccountingBusiness(iwc).insertCreditCardContract(getClub(), div, number, type);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void deleteContract(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);

		try {
			getAccountingBusiness(iwc).deleteContract(delete);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			saveContract(iwc);
		}
		else if (iwc.isParameterSet(ACTION_DELETE)) {
			deleteContract(iwc);
		}

		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();
		Table t = new Table();
		Table inputTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);

		int row = 1;
		Text labelDivision = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
		labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelContractNumber = new Text(iwrb.getLocalizedString(LABEL_CONTRACT_NUMBER, "Contract number"));
		labelContractNumber.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelCardType = new Text(iwrb.getLocalizedString(LABEL_CARD_TYPE, "Card type"));
		labelCardType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		inputTable.add(labelDivision, 1, row);
		inputTable.add(labelContractNumber, 2, row);
		inputTable.add(labelCardType, 3, row++);

		Collection col = null;
		Collection types = null;
		try {
			if (getClub() != null) {
				col = getAccountingBusiness(iwc).findAllCreditCardContractByClub(getClub());
				types = getAccountingBusiness(iwc).findAllCreditCardType();
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		DropdownMenu divInput = new DropdownMenu(LABEL_DIVISION);
		ArrayList divisions = new ArrayList();
		getClubDivisions(divisions, getClub());
		divInput.addMenuElement("-1", iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS, "All divisions"));
		if (divisions != null && !divisions.isEmpty()) {
			Iterator it = divisions.iterator();
			while (it.hasNext()) {
				Group div = (Group) it.next();
				divInput.addMenuElement(div.getPrimaryKey().toString(), div.getName());
			}
		}
		TextInput numberInput = new TextInput(LABEL_CONTRACT_NUMBER);
		DropdownMenu typeInput = new DropdownMenu(LABEL_CARD_TYPE);
		SelectorUtility util = new SelectorUtility();
		if (types != null && !types.isEmpty()) {
			typeInput = (DropdownMenu) util.getSelectorFromIDOEntities(typeInput, types, "getName");
		}
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		inputTable.add(divInput, 1, row);
		inputTable.add(numberInput, 2, row);
		inputTable.add(typeInput, 3, row);
		inputTable.add(submit, 4, row);

		row = 1;
		t.add(labelDivision, 2, row);
		t.add(labelContractNumber, 3, row);
		t.add(labelCardType, 4, row++);

		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				CreditCardContract cont = (CreditCardContract) it.next();
				CheckBox delete = new CheckBox(LABEL_DELETE, cont.getPrimaryKey().toString());
				t.add(delete, 1, row);
				Group div = cont.getDivision();
				if (div != null)
					t.add(div.getName(), 2, row);
				else
					t.add(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS, "All divisions"), 2, row);
				t.add(cont.getContractNumber(), 3, row);
				t.add(cont.getCardType().getName(), 4, row++);
			}

			SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
			delete.setToEnableWhenChecked(LABEL_DELETE);
			t.add(delete, 4, row);
			t.setAlignment(4, row, "RIGHT");
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

	private void getClubDivisions(Collection divisions, Group group) {
		if (divisions == null)
			divisions = new ArrayList();
		
		if (group.getGroupType().equals(IWMemberConstants.GROUP_TYPE_CLUB_DIVISION)) {
			divisions.add(group);
		}

		Iterator it = group.getChildren();
		if (it != null) {
			while (it.hasNext()) {
				Group child = (Group) it.next();
				getClubDivisions(divisions, child);
			}
		}
	}
}