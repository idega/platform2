/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;

import java.sql.Date;

import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * @author palli
 */
public interface PaymentContract extends IDOEntity {
    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setUser
     */
    public void setUser(User user);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setUserId
     */
    public void setUserId(int id);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setCardNumber
     */
    public void setCardNumber(String number);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setCardExpires
     */
    public void setCardExpires(Date expires);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setCardTypeId
     */
    public void setCardTypeId(int id);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setCardType
     */
    public void setCardType(CreditCardType type);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setFirstPayment
     */
    public void setFirstPayment(Date date);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#setNumberOfPayments
     */
    public void setNumberOfPayments(int nop);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getUser
     */
    public User getUser();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getUserId
     */
    public int getUserId();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getCardNumber
     */
    public String getCardNumber();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getCardExpires
     */
    public Date getCardExpires();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getCardTypeId
     */
    public int getCardTypeId();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getCardType
     */
    public CreditCardType getCardType();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getFirstPayment
     */
    public Date getFirstPayment();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.PaymentContractBMPBean#getNumberOfPayments
     */
    public int getNumberOfPayments();

}
