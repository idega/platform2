/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.presentation.CashierSubWindowTemplate;
import is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.SubmitButton;

/**
 * @author palli
 *
 */
public class CreditCardPlugin extends CashierSubWindowTemplate
		implements
			CheckoutPlugin {

    private static final String CONTRACT_SETUP = "ccp_contract_setup";
    
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
		Table t = new Table();
		t.setCellpadding(5);
	    
	    
		SubmitButton b = new SubmitButton("test", CONTRACT_SETUP, "true");
		
		t.add(b);
	    f.add(t);
	    
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