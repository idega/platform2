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
public class ConfigurationHomeImpl extends IDOFactory implements
        ConfigurationHome {
    protected Class getEntityInterfaceClass() {
        return Configuration.class;
    }

    public Configuration create() throws javax.ejb.CreateException {
        return (Configuration) super.createIDO();
    }

    public Configuration findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (Configuration) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((ConfigurationBMPBean) entity).ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Configuration findByCreditcardType(CreditCardType type)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ConfigurationBMPBean) entity)
                .ejbFindByCreditcardType(type);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

    public Configuration findByCreditcardTypeID(int typeID)
            throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((ConfigurationBMPBean) entity)
                .ejbFindByCreditcardTypeID(typeID);
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

}
