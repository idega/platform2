/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;


import com.idega.data.IDOFactory;

/**
 * @author palli
 */
public class PaymentContractHomeImpl extends IDOFactory implements
        PaymentContractHome {
    protected Class getEntityInterfaceClass() {
        return PaymentContract.class;
    }

    public PaymentContract create() throws javax.ejb.CreateException {
        return (PaymentContract) super.createIDO();
    }

    public PaymentContract findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (PaymentContract) super.findByPrimaryKeyIDO(pk);
    }

}
