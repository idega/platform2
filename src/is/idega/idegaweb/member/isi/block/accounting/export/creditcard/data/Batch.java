/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.sql.Timestamp;


import com.idega.data.IDOEntity;

/**
 * @author palli
 */
public interface Batch extends IDOEntity {
    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#setBatchNumber
     */
    public void setBatchNumber(String batchNumber);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#setCreated
     */
    public void setCreated(Timestamp created);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#setCreditcardTypeID
     */
    public void setCreditcardTypeID(int typeID);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#setCreditCardType
     */
    public void setCreditCardType(CreditCardType type);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#setContract
     */
    public void setContract(String contract);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#setFileName
     */
    public void setFileName(String fileName);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#setSent
     */
    public void setSent(Timestamp sent);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#getBatchNumber
     */
    public String getBatchNumber();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#getCreated
     */
    public Timestamp getCreated();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#getCreditcardTypeID
     */
    public int getCreditcardTypeID();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#getCreditCardType
     */
    public CreditCardType getCreditCardType();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#getContract
     */
    public String getContract();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#getFileName
     */
    public String getFileName();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data.BatchBMPBean#getSent
     */
    public Timestamp getSent();

}
