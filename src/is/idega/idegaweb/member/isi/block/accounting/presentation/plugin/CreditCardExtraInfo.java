/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.presentation.plugin;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;


/**
 * @author palli
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CreditCardExtraInfo {
    public CreditCardContract contract = null;
    
    public int amount[] = null;
    
    
    /**
     * 
     */
    public CreditCardExtraInfo(CreditCardContract contract, int amount[]) {
        this.contract = contract;
        this.amount = new int[amount.length];
        for (int i = 0; i < amount.length; i++) {
            this.amount[i] = amount[i];
        }
    }
}
