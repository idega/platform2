/*
 * Copyright (C) 2003 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.user.business.UserBusiness;
import com.idega.user.presentation.UserChooserBrowser;

/**
 * @author palli
 */
public class SelectPayments extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "sp_submit";
	protected static final String ACTION_ADD_TO_BASKET = "sp_basket";
	
	private final static String LABEL_SELECTED_USER = "isi_acc_sp_selected_user";

	private final static String LABEL_CLUB = "isi_acc_sp_club";
	private final static String LABEL_DIVISION = "isi_acc_sp_division";
	private final static String LABEL_INFO = "isi_acc_sp_info";
	private final static String LABEL_AMOUNT = "isi_acc_sp_amount";
	private final static String LABEL_AMOUNT_REMAINING = "isi_acc_sp_remaining";
	private final static String LABEL_ADD_TO_BASKET = "isi_acc_sp_add_basket";
	
	/**
	 * 
	 */
	public SelectPayments() {
		super();
	}

	public void main(IWContext iwc) {
		Form f = new Form();
		Table inputTable = new Table();
		Table paymentTable = new Table();
		inputTable.setCellpadding(5);
		paymentTable.setCellpadding(5);
			
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
		Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
		labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER, "Selected user:"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
		labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT, "Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelRemaining = new Text(iwrb.getLocalizedString(LABEL_AMOUNT_REMAINING, "Remaining"));
		labelRemaining.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");
		SubmitButton addToBasket = new SubmitButton(iwrb.getLocalizedString(ACTION_ADD_TO_BASKET, "Add to basket"), ACTION_ADD_TO_BASKET, "addToBasket");
		
		int row = 1;
		inputTable.add(labelUser, 1, row);
		inputTable.add(Text.getNonBrakingSpace(), 1, row);
		if (getUser() != null)
			inputTable.add(getUser().getName(), 1, row);
		
		row++;
		inputTable.add(new UserChooserBrowser(CashierWindow.PARAMETER_USER_ID), 1, row++);
		inputTable.add(submit, 1, row);
		
		inputTable.setAlignment(1, 3, "RIGHT");

		Collection entries = null;
		try {
			if (getClub() != null) {
				entries = getAccountingBusiness(iwc).findAllOpenAssessmentEntriesByUserGroupAndDivisione(getClub(), getDivision(), getUser());
			}
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}

		row = 1;
		paymentTable.add(labelClub, 2, row);
		paymentTable.add(labelDiv, 3, row);
		paymentTable.add(labelInfo, 4, row);
		paymentTable.add(labelAmount, 5, row);
		paymentTable.add(labelRemaining, 6, row++);
		
		if (entries != null && !entries.isEmpty()) {
		    Iterator it = entries.iterator();
		    while (it.hasNext()) {
		        FinanceEntry entry = (FinanceEntry) it.next();
		    }
		}
		
		f.add(inputTable);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
		
		add(f);
	}
	
	// service method
	private UserBusiness getUserBusiness(IWContext iwc) {
		try {
			return (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		}
		catch (RemoteException ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	//session business
	private BasketBusiness getBasketBusiness(IWContext iwc) {
	    try {
            return (BasketBusiness) IBOLookup.getSessionInstance(iwc, BasketBusiness.class);
        } catch (IBOLookupException e) {
			throw new RuntimeException(e.getMessage());
        }
	}
}