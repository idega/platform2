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

/**
 * @author palli
 */
public class PaymentTypeHomeImpl extends IDOFactory implements PaymentTypeHome {
    protected Class getEntityInterfaceClass() {
        return PaymentType.class;
    }

    public PaymentType create() throws javax.ejb.CreateException {
        return (PaymentType) super.createIDO();
    }

    public PaymentType findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (PaymentType) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAllPaymentTypes() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((PaymentTypeBMPBean) entity)
                .ejbFindAllPaymentTypes();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public PaymentType findPaymentTypeCreditcard() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((PaymentTypeBMPBean) entity)
                .ejbFindPaymentTypeCreditcard();
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

}
