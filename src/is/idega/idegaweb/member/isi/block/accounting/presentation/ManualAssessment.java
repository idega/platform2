/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.business.AccountingBusiness;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariff;
import is.idega.idegaweb.member.isi.block.accounting.data.ClubTariffHome;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.FloatInput;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;

/**
 * @author palli
 */
public class ManualAssessment extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "ma_submit";
	
	private final static String USER_CHOOSER_NAME = "ma_user_chooser_name";
	
	private final static String LABEL_SELECTED_USER = "isi_acc_ma_selected_user";
	private final static String LABEL_TARIFF = "isi_acc_ma_tariff";
	private final static String LABEL_AMOUNT = "isi_acc_ma_amount";
	private final static String LABEL_INFO = "isi_acc_ma_info";
	
	private final static String ERROR_NO_SELECTED_USER = "isi_acc_no_user_selected";
	
	/**
	 * 
	 */
	public ManualAssessment() {
		super();
	}

	public void main(IWContext iwc) {
		float defaultAmount = -1;
		if (iwc.isParameterSet(ACTION_SUBMIT)) {
			
		}
		else if (iwc.isParameterSet(LABEL_TARIFF)) {
			String id = iwc.getParameter(LABEL_TARIFF);
			try {
				ClubTariff tariff = getClubTariffHome().findByPrimaryKey(new Integer(id));
				defaultAmount = tariff.getAmount();
			}
			catch (NumberFormatException e) {
				e.printStackTrace();
			}
			catch (FinderException e) {
				e.printStackTrace();
			} 
		}
		
		Form f = new Form();
		Table t = new Table();
		Table inputTable = new Table();
		Table dataTable = new Table();
		t.setCellpadding(5);
		inputTable.setCellpadding(5);
		dataTable.setCellpadding(5);

		f.add(t);
		f.add(inputTable);
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER, "Selected user:"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		
		int row = 1;
		t.add(labelUser, 1, row);
		t.add(Text.getNonBrakingSpace(), 1, row);
		if (getUser() != null) {
			t.add(getUser().getName(), 1, row);
			
			row = 1;
			Text labelTariff = new Text(iwrb.getLocalizedString(LABEL_TARIFF, "Tariff"));
			labelTariff.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT, "Amount"));
			labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
			labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
			
			inputTable.add(labelTariff, 1, row);
			inputTable.add(labelAmount, 2, row);
			inputTable.add(labelInfo, 3, row++);
			
			Collection tariff = null;
			try {
				if (getClub() != null) {
					tariff = getAccountingBusiness(iwc).findAllTariffByClub(getClub());
				}
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			
			DropdownMenu tariffInput = new DropdownMenu(LABEL_TARIFF);
			if (tariff != null) {
				Iterator it = tariff.iterator();
				while (it.hasNext()) {
					ClubTariff entry = (ClubTariff) it.next();
					tariffInput.addMenuElement(((Integer) entry.getPrimaryKey()).intValue(), entry.getText());
				}
			}
			tariffInput.setToSubmit();
			
			FloatInput amountInput = new FloatInput(LABEL_AMOUNT);
			amountInput.setLength(10);
			if (defaultAmount > 0)
				amountInput.setValue(defaultAmount);
			TextInput infoInput = new TextInput(LABEL_INFO);
			infoInput.setLength(20);
			infoInput.setMaxlength(255);
			
			inputTable.add(tariffInput, 1, row);
			inputTable.add(amountInput, 2, row);
			inputTable.add(infoInput, 3, row);
			
			SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");
			inputTable.add(submit, 4, row++);
			
		}
		else {
			t.add(iwrb.getLocalizedString(ERROR_NO_SELECTED_USER, "No user selected. Please select a user in the Select user tab."), 1, row);
		}
			
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		
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
	
	private ClubTariffHome getClubTariffHome() {
		try {
			return (ClubTariffHome) IDOLookup.getHome(ClubTariff.class);
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}

		return null;
	}
}