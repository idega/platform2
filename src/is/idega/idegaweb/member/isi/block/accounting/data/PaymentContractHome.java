/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.data.IDOHome;

/**
 * @author palli
 */
public interface PaymentContractHome extends IDOHome {
    public PaymentContract create() throws javax.ejb.CreateException;

    public PaymentContract findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

}
