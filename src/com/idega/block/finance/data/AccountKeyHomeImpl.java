/*
 * $Id: AccountKeyHomeImpl.java,v 1.4 2004/11/17 22:50:55 aron Exp $
 * Created on 17.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.finance.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * 
 *  Last modified: $Date: 2004/11/17 22:50:55 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.4 $
 */
public class AccountKeyHomeImpl extends IDOFactory implements AccountKeyHome {
    protected Class getEntityInterfaceClass() {
        return AccountKey.class;
    }

    public AccountKey create() throws javax.ejb.CreateException {
        return (AccountKey) super.createIDO();
    }

    public AccountKey findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (AccountKey) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((AccountKeyBMPBean) entity).ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findBySQL(String sql) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((AccountKeyBMPBean) entity)
                .ejbFindBySQL(sql);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByCategory(Integer categoryID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((AccountKeyBMPBean) entity)
                .ejbFindByCategory(categoryID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findByPrimaryKeys(String[] keys) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((AccountKeyBMPBean) entity)
                .ejbFindByPrimaryKeys(keys);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
