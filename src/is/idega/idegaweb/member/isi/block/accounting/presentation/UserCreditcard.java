/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.UserCreditCard;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

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
public class UserCreditcard extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "ucc_submit";
	protected static final String ACTION_DELETE = "ucc_delete";
	
	protected final static String USER_CHOOSER_NAME = "ucc_user_chooser_name";
	
	protected final static String LABEL_SELECTED_USER = "isi_acc_ucc_select_user";
	
	protected final static String ERROR_NO_SELECTED_USER = "isi_acc_no_user_selected";

	protected final static String LABEL_DIVISION = "isi_acc_ucc_division";
	protected final static String LABEL_CARD_TYPE = "isi_acc_ucc_card_type";
	protected final static String LABEL_CARD_NO = "isi_acc_ucc_card_number";
	protected final static String LABEL_EXP_MONTH = "isi_acc_ucc_exp_month";
	protected final static String LABEL_EXP_YEAR = "isi_acc_ucc_exp_year";
	protected static final String LABEL_DELETE = "isi_acc_cccc_delete";
	
	protected static final String ELEMENT_ALL_DIVISIONS = "isi_acc_ucc_all_divisions";
	
	/**
	 * 
	 */
	public UserCreditcard() {
		super();
	}

	private void saveCreditCard(IWContext iwc) {
		String div = iwc.getParameter(LABEL_DIVISION);
		String type = iwc.getParameter(LABEL_CARD_TYPE);
		String number = iwc.getParameter(LABEL_CARD_NO);
		String month = iwc.getParameter(LABEL_EXP_MONTH);
		String year = iwc.getParameter(LABEL_EXP_YEAR);
		
		try {
			getAccountingBusiness(iwc).insertCreditCard(getClub(), div, type, number, month, year, getUser());
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private void deleteCreditCard(IWContext iwc) {
		String delete[] = iwc.getParameterValues(LABEL_DELETE);

		try {
			getAccountingBusiness(iwc).deleteCreditCards(delete);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void main(IWContext iwc) {
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			saveCreditCard(iwc);
		}
		else if (iwc.isParameterSet(ACTION_DELETE)) {
			deleteCreditCard(iwc);
		}
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Form f = new Form();
		Table t = new Table();
		Table inputTable = new Table();
		Table dataTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);
		dataTable.setCellpadding(5);
				
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER, "Selected user:"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		int row = 1;
		t.add(labelUser, 1, row);
		t.add(Text.getNonBrakingSpace(), 1, row);
		if (getUser() != null) {
			t.add(getUser().getName(), 1, row++);
			
			Text labelDivision = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
			labelDivision.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelCardType = new Text(iwrb.getLocalizedString(LABEL_CARD_TYPE, "Card type"));
			labelCardType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelCardNumber = new Text(iwrb.getLocalizedString(LABEL_CARD_NO, "Card number"));
			labelCardNumber.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelExpMonth = new Text(iwrb.getLocalizedString(LABEL_EXP_MONTH, "Expiration month"));
			labelExpMonth.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelExpYear = new Text(iwrb.getLocalizedString(LABEL_EXP_YEAR, "Expiration year"));
			labelExpYear.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			
			inputTable.add(labelDivision, 1, row);
			inputTable.add(labelCardType, 2, row);
			inputTable.add(labelCardNumber, 3, row);
			inputTable.add(labelExpMonth, 4, row);
			inputTable.add(labelExpYear, 5, row++);
			
			Collection userCreditCards = null;
			Collection types = null;
			try {
				if (getClub() != null) {
					userCreditCards = getAccountingBusiness(iwc).findAllUsersCreditCards(getClub(), getUser(), getDivision());
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
			
			DropdownMenu typeInput = new DropdownMenu(LABEL_CARD_TYPE);
			SelectorUtility util = new SelectorUtility();
			if (types != null && !types.isEmpty()) {
				typeInput = (DropdownMenu) util.getSelectorFromIDOEntities(typeInput, types, "getName");
			}

			TextInput numberInput = new TextInput(LABEL_CARD_NO);
			TextInput expMonth = new TextInput(LABEL_EXP_MONTH);
			TextInput expYear = new TextInput(LABEL_EXP_YEAR);
			
			SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");

			if (getDivision() == null) {
				inputTable.add(divInput, 1, row);
			}
			else {
				inputTable.add(getDivision().getName(), 1, row);
			}
			inputTable.add(typeInput, 2, row);
			inputTable.add(numberInput, 3, row);
			inputTable.add(expMonth, 4, row);
			inputTable.add(expYear, 5, row);
			inputTable.add(submit, 6, row);

			row = 1;
			dataTable.add(labelDivision, 2, row);
			dataTable.add(labelCardType, 3, row);
			dataTable.add(labelCardNumber, 4, row);
			dataTable.add(labelExpMonth, 5, row);
			dataTable.add(labelExpYear, 6, row++);
			
			if (userCreditCards != null && !userCreditCards.isEmpty()) {
				Iterator it = userCreditCards.iterator();
				while (it.hasNext()) {
					UserCreditCard credit = (UserCreditCard) it.next();
					CheckBox delete = new CheckBox(LABEL_DELETE, credit.getPrimaryKey().toString());
					dataTable.add(delete, 1, row);
					Group div = credit.getDivision();
					if (div != null)
						dataTable.add(div.getName(), 2, row);
					else
						dataTable.add(iwrb.getLocalizedString(ELEMENT_ALL_DIVISIONS, "All divisions"), 2, row);
					dataTable.add(credit.getCardType().getName(), 3, row);
					dataTable.add(credit.getCardNumber(), 4, row);
					dataTable.add(credit.getExpirationMonth(), 5, row);
					dataTable.add(credit.getExpirationYear(), 6, row++);
				}

				SubmitButton delete = new SubmitButton(iwrb.getLocalizedString(ACTION_DELETE, "Delete"), ACTION_DELETE, "delete");
				delete.setToEnableWhenChecked(LABEL_DELETE);
				dataTable.add(delete, 6, row);
				dataTable.setAlignment(6, row, "RIGHT");
			}
		}
		else {
			t.add(iwrb.getLocalizedString(ERROR_NO_SELECTED_USER, "No user selected. Please select a user in the Select user tab.."), 1, row);
		}
		
		f.add(t);
		f.add(inputTable);
		f.add(dataTable);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
		
		add(f);
	}
}