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
import com.idega.presentation.text.Text;

/**
 * @author palli
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreditCardPlugin extends CashierSubWindowTemplate
		implements
			CheckoutPlugin {

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#checkOut(com.idega.presentation.IWContext, java.lang.String, java.lang.String)
	 */
	public boolean checkOut(IWContext iwc, String type, String amount) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#showPlugin(com.idega.presentation.IWContext)
	 */
	public PresentationObject showPlugin(IWContext iwc) {
		Text testText = new Text("This is the creditcard plugin");
		
		return testText;
	}
	
	

}
