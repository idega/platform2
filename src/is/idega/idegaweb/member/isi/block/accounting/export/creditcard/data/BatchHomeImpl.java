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

import com.idega.data.IDOFactory;

/**
 * @author palli
 */
public class BatchHomeImpl extends IDOFactory implements BatchHome {
    protected Class getEntityInterfaceClass() {
        return Batch.class;
    }

    public Batch create() throws javax.ejb.CreateException {
        return (Batch) super.createIDO();
    }

    public Batch findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
        return (Batch) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((BatchBMPBean) entity).ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Batch findUnsentByContractNumberAndType(String contractNumber,
            CreditCardType type) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((BatchBMPBean) entity)
                .ejbFindUnsentByContractNumberAndType(contractNumber, type);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Collection findAllNewestFirst() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((BatchBMPBean) entity)
                .ejbFindAllNewestFirst();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
