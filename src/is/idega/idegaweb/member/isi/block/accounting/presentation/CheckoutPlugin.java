/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;

/**
 * A simple interface to define the functions a plugin in the checkout system must implement. Might move this to the basket block later.
 * 
 * @author palli
 */
public interface CheckoutPlugin {
    
    public static final String ACTION_PAY = "co_pay";
    
	public static final String LABEL_PAYMENT_TYPE = "isi_acc_co_payment_type";
    
	/**
	 * The business part of the checkout plugins. Should implement the actual checkout.
	 * 
	 * @param iwc The idegaWeb context parameter.
	 * @param type The payment type for this checkout.
	 * @param amount The amount that is paid.
	 * 
	 * @return True if the checkout is completed, false if there is an error or there are more than one step to the checkout.
	 */
    public boolean checkOut(IWContext iwc, String type, String amount);
    
    /**
     * The presentation part of the checkout plugins. Should return what is to be displayed for each step of a checkout.
     * 
     * @param iwc The idegaWeb context parameter.
     * 
     * @return A PresentationObject that shows what is happening at each step of a checkout procedure.
     */
    public PresentationObject showPlugin(IWContext iwc);
}
