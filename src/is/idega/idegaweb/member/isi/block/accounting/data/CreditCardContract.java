/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.data;



import com.idega.data.IDOEntity;
import com.idega.user.data.Group;

/**
 * @author palli
 */
public interface CreditCardContract extends IDOEntity {
    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setClubID
     */
    public void setClubID(int id);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setClub
     */
    public void setClub(Group club);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setDivisionId
     */
    public void setDivisionId(int id);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setDivision
     */
    public void setDivision(Group division);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setGroupId
     */
    public void setGroupId(int id);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setGroup
     */
    public void setGroup(Group group);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setContractNumber
     */
    public void setContractNumber(String number);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setCardTypeId
     */
    public void setCardTypeId(int id);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setCardType
     */
    public void setCardType(CreditCardType type);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#setDeleted
     */
    public void setDeleted(boolean deleted);

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getClubID
     */
    public int getClubID();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getClub
     */
    public Group getClub();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getDivisionId
     */
    public int getDivisionId();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getDivision
     */
    public Group getDivision();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getGroupId
     */
    public int getGroupId();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getGroup
     */
    public Group getGroup();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getContractNumber
     */
    public String getContractNumber();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getCardTypeId
     */
    public int getCardTypeId();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getCardType
     */
    public CreditCardType getCardType();

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#getDeleted
     */
    public boolean getDeleted();

}
