/*
 * $Id: BatchDeadlineHomeImpl.java,v 1.1 2004/11/01 17:00:25 aron Exp $
 * Created on 1.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOFactory;

/**
 * 
 *  Last modified: $Date: 2004/11/01 17:00:25 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public class BatchDeadlineHomeImpl extends IDOFactory implements
        BatchDeadlineHome {
    protected Class getEntityInterfaceClass() {
        return BatchDeadline.class;
    }

    public BatchDeadline create() throws javax.ejb.CreateException {
        return (BatchDeadline) super.createIDO();
    }

    public BatchDeadline findByPrimaryKey(Object pk)
            throws javax.ejb.FinderException {
        return (BatchDeadline) super.findByPrimaryKeyIDO(pk);
    }

    public Collection findAllCurrent() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        java.util.Collection ids = ((BatchDeadlineBMPBean) entity)
                .ejbFindAllCurrent();
        this.idoCheckInPooledEntity(entity);
        return this.getEntityCollectionForPrimaryKeys(ids);
    }

    public BatchDeadline findCurrent() throws FinderException {
        com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
        Object pk = ((BatchDeadlineBMPBean) entity).ejbFindCurrent();
        this.idoCheckInPooledEntity(entity);
        return this.findByPrimaryKey(pk);
    }

}
