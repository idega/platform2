/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.business;

import is.idega.idegaweb.member.isi.block.accounting.data.FinanceEntry;

import com.idega.data.IDOPrimaryKey;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author palli
 *
 */
public class FinanceExtraBasketInfo {

    public Group division = null;
    public Group group = null;
    public User user = null;
    public String info = null;
    public Double amount = null;
    public double amountPaid = 0.0;
    public IDOPrimaryKey key = null;
    
    public FinanceExtraBasketInfo(FinanceEntry entry, double amountPaid) {
        division = entry.getDivision();
        group = entry.getGroup();
        user = entry.getUser();
        info = entry.getInfo();
        amount = entry.getItemPrice();
        this.amountPaid = amountPaid;
        key = entry.getItemID();
    }
}