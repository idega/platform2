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
import java.text.NumberFormat;
import java.util.Iterator;
import java.util.Map;

import javax.ejb.EJBException;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.CheckBox;
import com.idega.presentation.ui.Form;
import com.idega.user.business.UserBusiness;

/**
 * @author palli
 */
public class Checkout extends CashierSubWindowTemplate {
	protected static final String ACTION_SUBMIT = "co_submit";
	protected static final String ACTION_REMOVE = "co_remove";
	
	private final static String LABEL_SELECTED_USER = "isi_acc_co_select_user";
	
	private final static String LABEL_USER = "isi_acc_co_user";
	private final static String LABEL_CLUB = "isi_acc_co_club";
	private final static String LABEL_DIVISION = "isi_acc_co_division";
	private final static String LABEL_INFO = "isi_acc_co_info";
	private final static String LABEL_AMOUNT = "isi_acc_co_amount";
	private final static String LABEL_AMOUNT_REMAINING = "isi_acc_co_remaining";
	
	private final static String LABEL_REMOVE_FROM_BASKET = "isi_acc_co_remove_from_basket";
	
	/**
	 * 
	 */
	public Checkout() {
		super();
	}

	/**
	 * 
	 */
	public void main(IWContext iwc) {
		Form f = new Form();
		IWResourceBundle iwrb = getResourceBundle(iwc);
		
//		if (iwc.isParameterSet(ACTION_ADD_TO_BASKET)) {
//			addToBasket(iwc);
//		}
		
		Table inputTable = new Table();
		Table paymentTable = new Table();
		inputTable.setCellpadding(5);
		paymentTable.setCellpadding(5);
					
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_SELECTED_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelClub = new Text(iwrb.getLocalizedString(LABEL_CLUB, "Club"));
		labelClub.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION, "Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
		labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT, "Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelRemaining = new Text(iwrb.getLocalizedString(LABEL_AMOUNT_REMAINING, "Remaining"));
		labelRemaining.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

//		SubmitButton submit = new SubmitButton(iwrb.getLocalizedString(ACTION_SUBMIT, "Submit"), ACTION_SUBMIT, "submit");
		
		int row = 1;
//		inputTable.add(labelUser, 1, row);
//		inputTable.add(Text.getNonBrakingSpace(), 1, row);
//		if (getUser() != null)
//			inputTable.add(getUser().getName(), 1, row);
//		
//		row++;
//		inputTable.add(new UserChooserBrowser(CashierWindow.PARAMETER_USER_ID), 1, row++);
//		inputTable.add(submit, 1, row);
//		
//		inputTable.setAlignment(1, 3, "RIGHT");

		Map entries = null; 
        try {
            entries = getBasketBusiness(iwc).getBasket();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        row = 1;
		paymentTable.add(labelUser, 2, row);
		paymentTable.add(labelClub, 3, row);
		paymentTable.add(labelDiv, 4, row);
		paymentTable.add(labelInfo, 5, row);
		paymentTable.add(labelAmount, 6, row);
		paymentTable.add(labelRemaining, 7, row++);
		
		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		nf.setMaximumFractionDigits(0);
		
		if (entries != null && !entries.isEmpty()) {
		    Iterator it = entries.keySet().iterator();
		    while (it.hasNext()) {
		        FinanceEntry entry = (FinanceEntry) ((BasketEntry) entries.get(it.next())).getItem();
                CheckBox removeFromBasket = new CheckBox(LABEL_REMOVE_FROM_BASKET, entry.getPrimaryKey().toString());
                paymentTable.add(removeFromBasket, 1, row);
                if (entry.getUser() != null) {
                    paymentTable.add(entry.getUser().getName(), 2, row);
                }
				if (entry.getClub() != null) {
				    paymentTable.add(entry.getClub().getName(), 3, row);
				}
				if (entry.getDivision() != null) {
				    paymentTable.add(entry.getDivision().getName(), 4, row);
				}
				paymentTable.add(entry.getInfo(), 5, row);
				paymentTable.add(nf.format(entry.getAmount()), 6, row);
				paymentTable.add(nf.format(entry.getAmount() - entry.getAmountEqualized()), 7, row);
				
				row++;
			}
			
//			SubmitButton removeFromBasket = new SubmitButton(iwrb.getLocalizedString(ACTION_ADD_TO_BASKET, "Add to basket"), ACTION_ADD_TO_BASKET, "add_to_basket");
//			moveToBasket.setToEnableWhenChecked(LABEL_ADD_TO_BASKET);
//			paymentTable.add(moveToBasket, 7, row);
			paymentTable.setAlignment(7, row, "RIGHT");
    
		    
		}
		
		f.add(inputTable);
		f.add(paymentTable);
		f.maintainParameter(CashierWindow.ACTION);
		f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
		f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
		f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
//		f.maintainParameter(CashierWindow.PARAMETER_USER_ID);
		
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