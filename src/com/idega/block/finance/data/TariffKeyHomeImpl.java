/*
 * $Id: TariffKeyHomeImpl.java,v 1.3 2004/11/17 22:50:55 aron Exp $
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
 * @version $Revision: 1.3 $
 */
public class TariffKeyHomeImpl extends IDOFactory implements TariffKeyHome {
    protected Class getEntityInterfaceClass() {
        return TariffKey.class;
    }

    public TariffKey create() throws javax.ejb.CreateException {
        return (TariffKey) super.createIDO();
    }

    public TariffKey findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (TariffKey) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findByCategory(Integer categoryID) throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((TariffKeyBMPBean) entity)
                .ejbFindByCategory(categoryID);
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public Collection findAll() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((TariffKeyBMPBean) entity).ejbFindAll();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

}
