/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author palli
 */
public interface PaymentTypeHome extends IDOHome {
    public PaymentType create() throws javax.ejb.CreateException;

    public PaymentType findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#ejbFindAllPaymentTypes
     */
    public Collection findAllPaymentTypes() throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentTypeBMPBean#ejbFindPaymentTypeCreditcard
     */
    public PaymentType findPaymentTypeCreditcard() throws FinderException;

}
