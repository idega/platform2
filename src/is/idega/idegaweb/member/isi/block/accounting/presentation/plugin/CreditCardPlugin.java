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
import java.util.Collection;

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
import com.idega.presentation.ui.SelectOption;
import com.idega.presentation.ui.TextInput;
import com.idega.presentation.ui.util.SelectorUtility;
import com.idega.util.DateFormatException;
import com.idega.util.IWTimestamp;

/**
 * @author palli
 *  
 */
public class CreditCardPlugin extends CashierSubWindowTemplate implements
        CheckoutPlugin {

    private static final String CONTRACT_SETUP = "isi_acc_ccp_contract_setup";

    private static final String LABEL_SSN = "isi_acc_ccp_ssn";

    private static final String LABEL_CARD_TYPE = "isi_acc_ccp_card_type";

    private static final String LABEL_CARD_NUMBER = "isi_acc_ccp_card_number";

    private static final String LABEL_CARD_EXPIRES = "isi_acc_ccp_card_expires";

    private static final String LABEL_CARD_VERIFICATION_CODE = "isi_acc_ccp_cvc";

    private static final String LABEL_NUMBER_OF_PAYMENTS = "isi_acc_ccp_number_of_payments";

    private static final String LABEL_DATE_OF_FIRST_PAYMENT = "isi_acc_ccp_first_payment";
    
    private static final String LABEL_AMOUNT = "isi_acc_ccp_amount";

    private static final String LABEL_RESULT = "isi_acc_ccp_result";

    /*
     * (non-Javadoc)
     * 
     * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#checkOut(com.idega.presentation.IWContext,
     *      java.lang.String, java.lang.String)
     */
    public boolean checkOut(IWContext iwc, String type, String amount) {
        boolean isContractDone = iwc.isParameterSet(CONTRACT_SETUP);

        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#showPlugin(com.idega.presentation.IWContext)
     */
    public PresentationObject showPlugin(IWContext iwc) {
        boolean isContractDone = iwc.isParameterSet(CONTRACT_SETUP);

        if (isContractDone) {
            return showReceipt(iwc);
        } else {
            return setupCreditCardContract(iwc);
        }
    }

    private PresentationObject setupCreditCardContract(IWContext iwc) {
        IWResourceBundle iwrb = getResourceBundle(iwc);
        Form f = new Form();
        Table inputTable = new Table();
        inputTable.setCellpadding(5);

        /*
         * ResultOutput result = new ResultOutput(LABEL_RESULT); DoubleInput
         * input1 = new DoubleInput("input1"); DoubleInput input2 = new
         * DoubleInput("input2");
         * 
         * result.add(input1); result.add(input2);
         * 
         * SubmitButton b = new SubmitButton("test", CONTRACT_SETUP, "true");
         * 
         * inputTable.add(input1); inputTable.add(input2);
         * inputTable.add(result); inputTable.add(b); f.add(inputTable);
         */

        String paramSSN = iwc.getParameter(LABEL_SSN);
        String paramCardType = iwc.getParameter(LABEL_CARD_TYPE);
        String paramCardNumber = iwc.getParameter(LABEL_CARD_NUMBER);
        String paramCardExpires = iwc.getParameter(LABEL_CARD_EXPIRES);
        String paramNumberOfPayments = iwc.getParameter(LABEL_NUMBER_OF_PAYMENTS);
        String paramDateOfFirstPayment = iwc.getParameter(LABEL_DATE_OF_FIRST_PAYMENT);
        
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
            } catch(IllegalArgumentException e) {
            }
        }
        inputTable.add(cardExpires, 4, row);
        
        DatePicker dateOfFirstPayment = new DatePicker(LABEL_DATE_OF_FIRST_PAYMENT, iwc.getCurrentLocale());
        if (paramDateOfFirstPayment != null) {
            IWTimestamp firstPayment = new IWTimestamp(paramDateOfFirstPayment);
            dateOfFirstPayment.setDate(firstPayment.getDate());
        }
        inputTable.add(dateOfFirstPayment, 5, row);

        DropdownMenu numberOfPayments = new DropdownMenu(LABEL_NUMBER_OF_PAYMENTS);
        for (int i = 1; i < 13; i++) {
            numberOfPayments.addOption(new SelectOption(Integer.toString(i), i));            
        }
        if (paramNumberOfPayments != null) {
            numberOfPayments.setSelectedElement(paramNumberOfPayments);
        }
        inputTable.add(numberOfPayments, 6, row);
        numberOfPayments.setToSubmit();
        
        row += 10;
        
        int nop = 1;
        if (paramNumberOfPayments != null) {
            nop = Integer.parseInt(paramNumberOfPayments);
        }
        
        IWTimestamp dofp = new IWTimestamp();
        if (paramDateOfFirstPayment != null) {
            try {
                dofp.setDate(paramDateOfFirstPayment);
            } catch (DateFormatException e1) {
                e1.printStackTrace();
            }
        }
        
        Text labelAmount[] = new Text[nop];
        labelAmount[0] = new Text(dofp.getDateString("dd.MM.yyyy"));
        labelAmount[0].setFontStyle(IWConstants.BUILDER_FONT_STYLE_LARGE);
        inputTable.add(labelAmount[0], 1, row);
        
        DoubleInput amount[] = new DoubleInput[nop];
        amount[0] = new DoubleInput(LABEL_AMOUNT + "_1");
        inputTable.add(amount[0], 2, row);
        
        for (int i = 2; i <= nop; i++) {
            
        }
        
        
        f.add(inputTable);
        
        f.maintainParameter(CashierWindow.ACTION);
        f.maintainParameter(CashierWindow.PARAMETER_GROUP_ID);
        f.maintainParameter(CashierWindow.PARAMETER_DIVISION_ID);
        f.maintainParameter(CashierWindow.PARAMETER_CLUB_ID);
        f.maintainParameter(ACTION_PAY);
        f.maintainParameter(LABEL_PAYMENT_TYPE);

        return f;
    }

    private PresentationObject showReceipt(IWContext iwc) {
        Table t = new Table();
        t.setCellpadding(5);

        Text testText = new Text("Contract is saved");

        t.add(testText);
        return t;
    }
}