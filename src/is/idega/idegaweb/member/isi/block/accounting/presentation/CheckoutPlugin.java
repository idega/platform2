/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package is.idega.idegaweb.member.isi.block.accounting.presentation;

import com.idega.presentation.IWContext;

/**
 * A simple interface to define the functions a plugin in the checkout system must implement. Might move this to the basket block later.
 * 
 * @author palli
 */
public interface CheckoutPlugin {
    public boolean checkOut(IWContext iwc, String type, String amount);
}
