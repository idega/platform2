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

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.IntegerInput;
import com.idega.presentation.ui.ResultOutput;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author palli
 *
 */
public class CreditCardPlugin extends CashierSubWindowTemplate
		implements
			CheckoutPlugin {

    private static final String CONTRACT_SETUP = "isi_acc_ccp_contract_setup";
    
    private static final String LABEL_SSN = "isi_acc_ccp_ssn";
    private static final String LABEL_CARD_TYPE = "isi_acc_ccp_card_type";
    private static final String LABEL_CARD_NUMBER = "isi_acc_ccp_card_number";
    private static final String LABEL_CARD_EXPIRES = "isi_acc_ccp_card_expires";
    private static final String LABEL_CARD_VERIFICATION_CODE = "isi_acc_ccp_cvc";
    private static final String LABEL_NUMBER_OF_PAYMENTS = "isi_acc_ccp_number_of_payments";
    private static final String LABEL_DATE_OF_FIRST_PAYMENT = "isi_acc_ccp_first_payment";
    
    private static final String LABEL_RESULT = "isi_acc_ccp_result";
    
	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#checkOut(com.idega.presentation.IWContext, java.lang.String, java.lang.String)
	 */
	public boolean checkOut(IWContext iwc, String type, String amount) {
	    boolean isContractDone = iwc.isParameterSet(CONTRACT_SETUP);

	    return false;
	}

	/* (non-Javadoc)
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
		Form f = new Form();
		Table inputTable = new Table();
		inputTable.setCellpadding(5);
	    
	    ResultOutput result = new ResultOutput(LABEL_RESULT);
	    IntegerInput input1 = new IntegerInput("input1");
	    IntegerInput input2 = new IntegerInput("input2");
	    
	    result.add(input1);
	    result.add(input2);
		
		SubmitButton b = new SubmitButton("test", CONTRACT_SETUP, "true");
		
		inputTable.add(input1);
		inputTable.add(input2);
		inputTable.add(result);
		inputTable.add(b);
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