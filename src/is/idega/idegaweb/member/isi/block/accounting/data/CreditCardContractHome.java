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
import com.idega.user.data.Group;

/**
 * @author palli
 */
public interface CreditCardContractHome extends IDOHome {
    public CreditCardContract create() throws javax.ejb.CreateException;

    public CreditCardContract findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#ejbFindAll
     */
    public Collection findAll() throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#ejbFindAllByClub
     */
    public Collection findAllByClub(Group club) throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#ejbFindAllByClubAndType
     */
    public Collection findAllByClubAndType(Group club, CreditCardType type)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#ejbFindAllByClubDivisionAndType
     */
    public Collection findAllByClubDivisionAndType(Group club, Group division,
            CreditCardType type) throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#ejbFindAllByClubDivisionGroupAndType
     */
    public Collection findAllByClubDivisionGroupAndType(Group club,
            Group division, Group group, CreditCardType type)
            throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#ejbFindAllClubContracts
     */
    public Collection findAllClubContracts() throws FinderException;

    /**
     * @see is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContractBMPBean#ejbFindByGroupAndType
     */
    public CreditCardContract findByGroupAndType(Group group,
            CreditCardType type) throws FinderException;

}
