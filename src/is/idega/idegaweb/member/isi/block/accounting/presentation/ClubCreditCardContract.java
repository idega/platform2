/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;

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
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.data.Group;
import com.idega.user.presentation.GroupChooser;

/**
 * @author palli
 */
public class ClubCreditCardContract extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "etl_submit";

	protected static final String LABEL_CLUB = "cccc_club";
	protected static final String LABEL_DIVISION = "cccc_division";
	protected static final String LABEL_CONTRACT_NUMBER = "cccc_cont_nr";
	protected static final String LABEL_CARD_TYPE = "cccc_card_type";

	/**
	 *  
	 */
	public ClubCreditCardContract() {
		super();
	}

	private void saveTariffEntry(IWContext iwc) {
		String div = iwc.getParameter(LABEL_DIVISION);
		String number = iwc.getParameter(LABEL_CONTRACT_NUMBER);
		String type = iwc.getParameter(LABEL_CARD_TYPE);

		if (div != null) {
			div = div.substring(div.indexOf("_")+1);
		}

		try {
			System.out.println("Inserting entry");
			getAccountingBusiness(iwc).insertCreditCardContract(getClub(),div,number,type); 
		}
		catch (RemoteException e) { 
			e.printStackTrace();
		}
	}

	public void main(IWContext iwc) {
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			saveTariffEntry(iwc);
		}

		IWResourceBundle iwrb = getResourceBundle(iwc);

		Form f = new Form();
		Table t = new Table();
		t.setCellpadding(5);

		int row = 1;
		Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
		labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDivision = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
		labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelContractNumber = new Text(iwrb.getLocalizedString(LABEL_CONTRACT_NUMBER, "Contract number"));
		labelContractNumber.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelCardType = new Text(iwrb.getLocalizedString(LABEL_CARD_TYPE, "Card type"));
		labelCardType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		t.add(labelClub, 2, row);
		t.add(labelDivision, 3, row);
		t.add(labelContractNumber, 4, row);
		t.add(labelCardType, 5, row++);
		
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

		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			while (it.hasNext()) {
				CreditCardContract cont = (CreditCardContract) it.next();
				t.add(getClub().getName(),2,row);
				Group div = cont.getDivision();
				if (div != null)
					t.add(div.getName(), 3, row);
				t.add(cont.getContractNumber(), 4, row);
				t.add(cont.getCardType().getName(), 5, row++);
			}
		}

		row += 6;

		GroupChooser divInput = new GroupChooser(LABEL_DIVISION);
		TextInput numberInput = new TextInput(LABEL_CONTRACT_NUMBER);
		DropdownMenu typeInput = new DropdownMenu(LABEL_CARD_TYPE);
		SelectorUtility util = new SelectorUtility();
		if (types != null && !types.isEmpty()) {
			typeInput = (DropdownMenu) util.getSelectorFromIDOEntities(typeInput, types, "getName");
		}
		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

		t.add(divInput, 3, row);
		t.add(numberInput,4,row);
		t.add(typeInput, 5, row);
		t.add(submit, 6, row);

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