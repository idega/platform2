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

import com.idega.data.IDOFactory;
import com.idega.user.data.Group;

/**
 * @author palli
 */
public class CreditCardContractHomeImpl extends IDOFactory implements
        CreditCardContractHome {
    protected Class getEntityInterfaceClass() {
        return CreditCardContract.class;
    }

    public CreditCardContract create() throws javax.ejb.CreateException {
        return (CreditCardContract) super.createIDO();
    }

    public CreditCardContract findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (CreditCardContract) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((CreditCardContractBMPBean) entity)
                .ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByClub(Group club) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((CreditCardContractBMPBean) entity)
                .ejbFindAllByClub(club);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByClubAndType(Group club, CreditCardType type)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((CreditCardContractBMPBean) entity)
                .ejbFindAllByClubAndType(club, type);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByClubDivisionAndType(Group club, Group division,
            CreditCardType type) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((CreditCardContractBMPBean) entity)
                .ejbFindAllByClubDivisionAndType(club, division, type);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllByClubDivisionGroupAndType(Group club,
            Group division, Group group, CreditCardType type)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((CreditCardContractBMPBean) entity)
                .ejbFindAllByClubDivisionGroupAndType(club, division, group,
                        type);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAllClubContracts() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((CreditCardContractBMPBean) entity)
                .ejbFindAllClubContracts();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public CreditCardContract findByGroupAndType(Group group,
            CreditCardType type) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((CreditCardContractBMPBean) entity).ejbFindByGroupAndType(
                group, type);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

}
