/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.business.FinanceExtraBasketInfo;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierSubWindowTemplate;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.user.business.UserBusiness;

/**
 * @author palli
 *  
 */
public class BankPlugin extends CashierSubWindowTemplate implements
        CheckoutPlugin {

    private static final String ACTION_CONTRACT_SETUP = "isi_acc_ccp_contract_setup";

    private static final String LABEL_SSN = "isi_acc_ccp_ssn";

    private static final String LABEL_CARD_TYPE = "isi_acc_ccp_card_type";

    private static final String LABEL_CARD_NUMBER = "isi_acc_ccp_card_number";

    private static final String LABEL_CARD_EXPIRES = "isi_acc_ccp_card_expires";

    private static final String LABEL_CARD_VERIFICATION_CODE = "isi_acc_ccp_cvc";

    //private static final String LABEL_NUMBER_OF_PAYMENTS = "isi_acc_ccp_number_of_payments";

    //private static final String LABEL_DATE_OF_FIRST_PAYMENT = "isi_acc_ccp_first_payment";

    private static final String LABEL_AMOUNT = "isi_acc_ccp_amount";

    private static final String LABEL_RESULT = "isi_acc_ccp_result";

    protected static final String LABEL_RECEIPT = "isi_acc_ccp_receipt";

    protected static final String LABEL_DIVISION = "isi_acc_ccp_div";

    protected static final String LABEL_GROUP = "isi_acc_ccp_group";

    protected static final String LABEL_INFO = "isi_acc_ccp_info";

    protected static final String LABEL_USER = "isi_acc_ccp_user";

    protected static final String LABEL_AMOUNT_PAID = "isi_acc_ccp_amount_paid";

    protected static final String LABEL_SUM = "isi_acc_ccp_sum";
    
    protected static final String LABEL_DUE_DATE = "isi_acc_due_date";
    
    protected static final String LABEL_FINAL_DUE_DATE = "isi_acc_final_due_date";

    private static final String ERROR_NO_SSN_ENTERED = "isi_acc_ccp_no_ssn_entered";

    private static final String ERROR_SSN_NOT_FOUND = "isi_acc_ccp_ssn_not_found";

    /*
     * (non-Javadoc)
     * 
     * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#checkOut(com.idega.presentation.IWContext,
     *      java.lang.String, java.lang.String)
     */
    public boolean checkOut(IWContext iwc, String type, String amount) {
        boolean isContractDone = iwc.isParameterSet(ACTION_CONTRACT_SETUP);

        if (isContractDone) {
            errorList = new ArrayList();
            String paramSSN = iwc.getParameter(LABEL_SSN);

            if (paramSSN == null || "".equals(paramSSN)) {
                errorList.add(ERROR_NO_SSN_ENTERED);
            } 

            if (!errorList.isEmpty()) { return false; }

            try {
            		Map basket = getBasketBusiness(iwc).getBasket();
            		getAccountingBusiness(iwc).insertPayment(type, amount,
					iwc.getCurrentUser(), basket, iwc, paramSSN);


                return true;
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#showPlugin(com.idega.presentation.IWContext)
     */
    public PresentationObject showPlugin(IWContext iwc, String type,
            String amount) {
        boolean isContractDone = iwc.isParameterSet(ACTION_CONTRACT_SETUP);
        if (errorList != null && !errorList.isEmpty()) {
            isContractDone = false;
        }

        if (isContractDone) {
            return showReceipt(iwc);
        } else {
            return getPaymentBy(iwc, amount);
        }
    }

    private PresentationObject getPaymentBy(IWContext iwc,
            String amountString) {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        Form f = new Form();
        Table inputTable = new Table();
        inputTable.setCellpadding(5);

        if (errorList != null) {
            Table error = new Table();
            Text labelError = new Text(iwrb.getLocalizedString(
                    ERROR_COULD_NOT_SAVE, "Could not save")
                    + ":");
            labelError.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

            int r = 1;
            error.add(labelError, 1, r++);
            if (errorList != null && !errorList.isEmpty()) {
                Iterator it = errorList.iterator();
                while (it.hasNext()) {
                    String loc = (String) it.next();
                    Text errorText = new Text(iwrb.getLocalizedString(loc, ""));
                    errorText
                            .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                    error.add(errorText, 1, r++);
                }
            }

            f.add(error);
        }

        String paramSSN = iwc.getParameter(LABEL_SSN);

        int row = 1;
        Text labelSSN = new Text(iwrb.getLocalizedString(LABEL_SSN, "SSN"));
        labelSSN.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        inputTable.add(labelSSN, 1, row);

        TextInput ssn = new TextInput(LABEL_SSN);
        if (paramSSN != null) {
            ssn.setValue(paramSSN);
        }
        inputTable.add(ssn, 1, row);

        row += 2;

        SubmitButton submitContract = new SubmitButton(iwrb.getLocalizedString(
                ACTION_CONTRACT_SETUP, "Submit contract"),
                ACTION_CONTRACT_SETUP, "submit");

        inputTable.add(submitContract, 1, row);

        f.add(inputTable);

        f.maintainParameter(CashierWindow.ACTION);
        f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
        f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
        f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
        f.maintainParameter(ACTION_PAY);
        f.maintainParameter(LABEL_PAYMENT_TYPE);
        f.maintainParameter(LABEL_TO_PAY);

        return f;
    }

    private PresentationObject showReceipt(IWContext iwc) {
		Table returnObject = new Table();
		
		IWResourceBundle iwrb = getResourceBundle(iwc);
		Table t = new Table();
		t.setCellpadding(5);

		int row = 1;
		Text labelDiv = new Text(iwrb.getLocalizedString(LABEL_DIVISION,
				"Division"));
		labelDiv.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelGroup = new Text(iwrb
				.getLocalizedString(LABEL_GROUP, "Group"));
		labelGroup.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelUser = new Text(iwrb.getLocalizedString(LABEL_USER, "User"));
		labelUser.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelInfo = new Text(iwrb.getLocalizedString(LABEL_INFO, "Info"));
		labelInfo.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmount = new Text(iwrb.getLocalizedString(LABEL_AMOUNT,
				"Amount"));
		labelAmount.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelAmountPaid = new Text(iwrb.getLocalizedString(LABEL_AMOUNT_PAID,
		"Amount paid"));
		labelAmountPaid.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
		Text labelSum = new Text(iwrb.getLocalizedString(LABEL_SUM, "Sum"));
		labelSum.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

		t.add(labelDiv, 1, row);
		t.add(labelGroup, 2, row);
		t.add(labelUser, 3, row);		
		t.add(labelInfo, 4, row);
		t.add(labelAmount, 5, row);
		t.add(labelAmountPaid, 6, row);
		row++;

		NumberFormat nf = NumberFormat.getInstance(iwc.getCurrentLocale());
		nf.setMaximumFractionDigits(0);

		try {
			List paid = null;
            try {
                paid = getBasketBusiness(iwc).getExtraInfo();
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            
            if (paid != null && !paid.isEmpty()) {
				Iterator it = paid.iterator();
				double sum = 0;
				while (it.hasNext()) {
					FinanceExtraBasketInfo info = (FinanceExtraBasketInfo) it.next();
					if (info.getDivision() != null) {
						t.add(info.getDivision().getName(), 1, row);
					}
					if (info.getGroup() != null) {
					    t.add(info.getGroup().getName(), 2, row);
					}
					t.add(info.getUser().getName(), 3, row);
					if (info.getInfo() != null) {
						t.add(info.getInfo(), 4, row);
					}
					t.add(nf.format(info.getAmount().doubleValue()), 5, row);
					t.setAlignment(5, row, "RIGHT");
					t.add(nf.format(info.getAmountPaid()), 6, row);
					t.setAlignment(6, row, "RIGHT");
					sum += info.getAmountPaid();
					row++;
				}
				t.mergeCells(1, row, 6, row);
				t.add("<hr>", 1, row++);
				t.add(labelSum, 5, row);
				t.add(nf.format(sum), 6, row);
				t.setAlignment(6, row, "RIGHT");
			}            
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		returnObject.add(t);
		Link receipt = new Link(iwrb.getLocalizedString(LABEL_RECEIPT,"Receipt"));
		receipt.addParameter("receipt_payment_type", iwc.getParameter(LABEL_PAYMENT_TYPE));
		receipt.addParameter("receipt_ssn", iwc.getParameter(LABEL_SSN));
		receipt.setWindowToOpen(DefaultCheckoutReceiptWindow.class);
		returnObject.add(receipt);

		return returnObject;
    }

    private UserBusiness getUserBusiness(IWApplicationContext iwc) {
        try {
            return (UserBusiness) IBOLookup.getServiceInstance(iwc,
                    UserBusiness.class);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return null;
    }

    //session business
    private BasketBusiness getBasketBusiness(IWContext iwc) {
        try {
            return (BasketBusiness) IBOLookup.getSessionInstance(iwc,
                    BasketBusiness.class);
        } catch (IBOLookupException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}