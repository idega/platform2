/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOHome;

/**
 * @author palli
 */
public interface BatchHome extends IDOHome {
    public Batch create() throws javax.ejb.CreateException;

    public Batch findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#ejbFindUnsentByContractNumberAndType
     */
    public Batch findUnsentByContractNumberAndType(String contractNumber,
            CreditCardType type) throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#ejbFindAllNewestFirst
     */
    public Collection findAllNewestFirst() throws FinderException;

}
