/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierSubWindowTemplate;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierWindow;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWConstants;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.ui.DatePicker;
import com.idega.presentation.ui.DoubleInput;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.ResultOutput;
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 *  
 */
public class CreditCardPlugin extends CashierSubWindowTemplate implements
        CheckoutPlugin {

    private static final String ACTION_CONTRACT_SETUP = "isi_acc_ccp_contract_setup";

    private static final String LABEL_SSN = "isi_acc_ccp_ssn";

    private static final String LABEL_CARD_TYPE = "isi_acc_ccp_card_type";

    private static final String LABEL_CARD_NUMBER = "isi_acc_ccp_card_number";

    private static final String LABEL_CARD_EXPIRES = "isi_acc_ccp_card_expires";

    private static final String LABEL_CARD_VERIFICATION_CODE = "isi_acc_ccp_cvc";

    private static final String LABEL_NUMBER_OF_PAYMENTS = "isi_acc_ccp_number_of_payments";

    private static final String LABEL_DATE_OF_FIRST_PAYMENT = "isi_acc_ccp_first_payment";

    private static final String LABEL_AMOUNT = "isi_acc_ccp_amount";

    private static final String LABEL_RESULT = "isi_acc_ccp_result";

    private static final String ERROR_NO_SSN_ENTERED = "isi_acc_ccp_no_ssn_entered";

    private static final String ERROR_SSN_NOT_FOUND = "isi_acc_ccp_ssn_not_found";
    
    private static final String ERROR_NO_CARD_TYPE_SELECTED = "isi_acc_ccp_no_type_selected";

    private static final String ERROR_NO_CARD_NUMBER_ENTERED = "isi_acc_ccp_no_card_number_entered";

    private static final String ERROR_NO_EXP_ENTERED = "isi_acc_ccp_no_exp_entered";
    
    private static final String ERROR_NO_NOP_SELECTED = "isi_acc_ccp_no_nop_selected";

    private static final String ERROR_NO_DOFP_ENTERED = "isi_acc_ccp_no_dofp_entered";

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
            String paramCardType = iwc.getParameter(LABEL_CARD_TYPE);
            String paramCardNumber = iwc.getParameter(LABEL_CARD_NUMBER);
            String paramCardExpires = iwc.getParameter(LABEL_CARD_EXPIRES);
            String paramNumberOfPayments = iwc
                    .getParameter(LABEL_NUMBER_OF_PAYMENTS);
            String paramDateOfFirstPayment = iwc
                    .getParameter(LABEL_DATE_OF_FIRST_PAYMENT);

            if (paramSSN == null || "".equals(paramSSN)) {
                errorList.add(ERROR_NO_SSN_ENTERED);
            } else {
                User user = null;
                try {
                    user = getUserBusiness(iwc).getUser(paramSSN);
                } catch (FinderException e) {
                    user = null;
                }
                
                if (user == null) {
                    errorList.add(ERROR_SSN_NOT_FOUND);
                }
            }
            
            if (paramCardType == null || "".equals(paramCardType)) {
                errorList.add(ERROR_NO_CARD_TYPE_SELECTED);
            }

            if (paramCardNumber == null || "".equals(paramCardNumber)) {
                errorList.add(ERROR_NO_CARD_NUMBER_ENTERED);
            }
            
            IWTimestamp expires = null;
            IWTimestamp dofp = null;
            
            try {
                expires = new IWTimestamp(paramCardExpires);
            } catch (IllegalArgumentException e) {
                expires = null;
            }
            
            if (expires == null) {
                errorList.add(ERROR_NO_EXP_ENTERED);
            }
            
            if (paramNumberOfPayments == null || "".equals(paramNumberOfPayments)) {
                errorList.add(ERROR_NO_NOP_SELECTED);
            }

            try {
                dofp = new IWTimestamp(paramDateOfFirstPayment);
            } catch (IllegalArgumentException e) {
                dofp = new IWTimestamp(Long.parseLong(paramDateOfFirstPayment));
                dofp.setHour(0);
                dofp.setMinute(0);
                dofp.setSecond(0);
                dofp.setMilliSecond(0);
            }

            if (dofp == null) {
                errorList.add(ERROR_NO_DOFP_ENTERED);                
            }
            
            if (!errorList.isEmpty()) {
                return false;
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
            return setupCreditCardContract(iwc, amount);
        }
    }

    private PresentationObject setupCreditCardContract(IWContext iwc,
            String amountString) {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        Form f = new Form();
        Table inputTable = new Table();
        inputTable.setCellpadding(5);
        Table amountTable = new Table();
        amountTable.setCellpadding(5);

        if (errorList != null) {
            Table error = new Table();
            Text labelError = new Text(iwrb.getLocalizedString(
                    ERROR_COULD_NOT_SAVE, "Could not save")
                    + ":");
            labelError
                    .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

            int r = 1;
            error.add(labelError, 1, r++);
            if (errorList != null && !errorList.isEmpty()) {
                Iterator it = errorList.iterator();
                while (it.hasNext()) {
                    String loc = (String) it.next();
                    Text errorText = new Text(iwrb.getLocalizedString(loc,
                            ""));
                    errorText
                            .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE_RED);

                    error.add(errorText, 1, r++);
                }
            }

            f.add(error);
        }
        
        String paramSSN = iwc.getParameter(LABEL_SSN);
        String paramCardType = iwc.getParameter(LABEL_CARD_TYPE);
        String paramCardNumber = iwc.getParameter(LABEL_CARD_NUMBER);
        String paramCardExpires = iwc.getParameter(LABEL_CARD_EXPIRES);
        String paramNumberOfPayments = iwc
                .getParameter(LABEL_NUMBER_OF_PAYMENTS);
        String paramDateOfFirstPayment = iwc
                .getParameter(LABEL_DATE_OF_FIRST_PAYMENT);

        int row = 1;
        Text labelSSN = new Text(iwrb.getLocalizedString(LABEL_SSN, "SSN"));
        labelSSN.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelCardType = new Text(iwrb.getLocalizedString(LABEL_CARD_TYPE,
                "Card type"));
        labelCardType.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelCardNumber = new Text(iwrb.getLocalizedString(
                LABEL_CARD_NUMBER, "Card number"));
        labelCardNumber.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelCardExpires = new Text(iwrb.getLocalizedString(
                LABEL_CARD_EXPIRES, "Card expires"));
        labelCardExpires.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelNumberOfPayments = new Text(iwrb.getLocalizedString(
                LABEL_NUMBER_OF_PAYMENTS, "Number of payments"));
        labelNumberOfPayments
                .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelDateOfFirstPayment = new Text(iwrb.getLocalizedString(
                LABEL_DATE_OF_FIRST_PAYMENT, "Date of first payment"));
        labelDateOfFirstPayment
                .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        Text labelResult = new Text(iwrb.getLocalizedString(LABEL_RESULT,
                "Result"));
        labelResult.setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

        inputTable.add(labelSSN, 1, row);
        inputTable.add(labelCardType, 2, row);
        inputTable.add(labelCardNumber, 3, row);
        inputTable.add(labelCardExpires, 4, row);
        inputTable.add(labelDateOfFirstPayment, 5, row);
        inputTable.add(labelNumberOfPayments, 6, row++);

        TextInput ssn = new TextInput(LABEL_SSN);
        if (paramSSN != null) {
            ssn.setValue(paramSSN);
        }
        inputTable.add(ssn, 1, row);

        Collection types = null;
        try {
            types = getAccountingBusiness(iwc).findAllCreditCardType();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        DropdownMenu typeInput = new DropdownMenu(LABEL_CARD_TYPE);
        SelectorUtility util = new SelectorUtility();
        if (types != null && !types.isEmpty()) {
            typeInput = (DropdownMenu) util.getSelectorFromIDOEntities(
                    typeInput, types, "getName");
        }
        if (paramCardType != null) {
            typeInput.setSelectedElement(paramCardType);
        }
        inputTable.add(typeInput, 2, row);

        TextInput cardNumber = new TextInput(LABEL_CARD_NUMBER);
        if (paramCardNumber != null) {
            cardNumber.setValue(paramCardNumber);
        }
        inputTable.add(cardNumber, 3, row);

        DateInput cardExpires = new DateInput(LABEL_CARD_EXPIRES, true);
        cardExpires.setNoDayView();
        if (paramCardExpires != null) {
            try {
                IWTimestamp expires = new IWTimestamp(paramCardExpires);
                cardExpires.setDate(expires.getDate());
            } catch (IllegalArgumentException e) {
            }
        }
        inputTable.add(cardExpires, 4, row);

        DatePicker dateOfFirstPayment = new DatePicker(
                LABEL_DATE_OF_FIRST_PAYMENT, iwc.getCurrentLocale());
        if (paramDateOfFirstPayment != null) {
            IWTimestamp firstPayment = new IWTimestamp(paramDateOfFirstPayment);
            dateOfFirstPayment.setDate(firstPayment.getDate());
        }
        inputTable.add(dateOfFirstPayment, 5, row);

        DropdownMenu numberOfPayments = new DropdownMenu(
                LABEL_NUMBER_OF_PAYMENTS);
        for (int i = 1; i < 13; i++) {
            numberOfPayments
                    .addOption(new SelectOption(Integer.toString(i), i));
        }
        if (paramNumberOfPayments != null) {
            numberOfPayments.setSelectedElement(paramNumberOfPayments);
        }
        inputTable.add(numberOfPayments, 6, row);
        numberOfPayments.setToSubmit();

        row = 1;

        int nop = 1;
        if (paramNumberOfPayments != null) {
            nop = Integer.parseInt(paramNumberOfPayments);
        }

        IWTimestamp dofp = null;
        if (paramDateOfFirstPayment != null) {
            try {
                dofp = new IWTimestamp(paramDateOfFirstPayment);
            } catch (IllegalArgumentException e1) {
            }
        }
        
        if (dofp == null) {
            dofp = new IWTimestamp();
        }

        Text labelAmount[] = new Text[nop];
        labelAmount[0] = new Text(dofp.getDateString("dd.MM.yyyy"));
        labelAmount[0].setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        amountTable.add(labelAmount[0], 1, row);

        DoubleInput amount[] = new DoubleInput[nop];
        double totalAmount = Double.parseDouble(amountString);
        long prMonth = Math.round(totalAmount / nop);
        double sum = 0.0;
        for (int j = 1; j <= nop; j++) {
            amount[j - 1] = new DoubleInput(LABEL_AMOUNT + "_" + j);
            amount[j - 1].setLength(10);
            if (j == nop) {
                amount[j - 1].setValue(totalAmount - sum);
            } else {
                amount[j - 1].setValue(prMonth);
            }

            sum += prMonth;
        }
        amountTable.add(amount[0], 1, row + 1);

        ResultOutput result = new ResultOutput(LABEL_RESULT, amountString);
        result.add(amount[0]);
        result.setSize(10);
        
        for (int i = 2; i <= nop; i++) {
            IWTimestamp next = new IWTimestamp(dofp);
            next.addMonths(i - 1);
            labelAmount[i - 1] = new Text(next.getDateString("dd.MM.yyyy"));
            labelAmount[i - 1]
                    .setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);

            if (i == 7) {
                row += 2;
            }

            if (i <= 6) {
                amountTable.add(labelAmount[i - 1], i, row);
                amountTable.add(amount[i - 1], i, row + 1);
            } else {
                amountTable.add(labelAmount[i - 1], i - 6, row);
                amountTable.add(amount[i - 1], i - 6, row + 1);
            }

            result.add(amount[i - 1]);
        }
        
        row += 2;
        
        SubmitButton submitContract = new SubmitButton(iwrb.getLocalizedString(
                ACTION_CONTRACT_SETUP, "Submit contract"), ACTION_CONTRACT_SETUP, "submit");

        if (nop < 6) {
            amountTable.add(labelResult, nop, row);
            amountTable.add(result, nop, row + 1);
            amountTable.add(submitContract, nop + 1, row + 1);
        } else {
            amountTable.add(labelResult, 6, row);
            amountTable.add(result, 6, row + 1);            
            amountTable.add(submitContract, 7, row + 1);
        }
        


        f.add(inputTable);
        f.add(amountTable);

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
        Table t = new Table();
        t.setCellpadding(5);

        Text testText = new Text("Contract is saved");

        t.add(testText);
        return t;
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
}