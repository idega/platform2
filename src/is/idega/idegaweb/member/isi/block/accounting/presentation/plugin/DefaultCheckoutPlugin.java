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


/**
 * The simple default plugin for the payment types that do not specify a plugin. Just displays a list of the entries paid and a print button.
 * 
 * @author palli
 *
 */
public class DefaultCheckoutPlugin extends CashierSubWindowTemplate implements
        CheckoutPlugin {

    
    
    /* (non-Javadoc)
     * @see is.idega.idegaweb.member.isi.block.accounting.presentation.CheckoutPlugin#checkOut(com.idega.presentation.IWContext, java.lang.String, java.lang.String)
     */
    public boolean checkOut(IWContext iwc, String type, String amount) {
        
        
        
        return false;
    }

}
