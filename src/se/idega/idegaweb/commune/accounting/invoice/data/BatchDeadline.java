/*
 * $Id: BatchDeadline.java,v 1.1 2004/11/01 17:00:25 aron Exp $
 * Created on 1.11.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Timestamp;


import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOStoreException;

/**
 * 
 *  Last modified: $Date: 2004/11/01 17:00:25 $ by $Author: aron $
 * 
 * @author <a href="mailto:aron@idega.com">aron</a>
 * @version $Revision: 1.1 $
 */
public interface BatchDeadline extends IDOEntity {
    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#setDeadlineDay
     */
    public void setDeadlineDay(int day);

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#getDeadlineDay
     */
    public int getDeadlineDay();

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#getCreated
     */
    public Timestamp getCreated();

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#setCreated
     */
    public void setCreated(Timestamp created);

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#isCurrent
     */
    public boolean isCurrent();

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#setIsCurrent
     */
    public void setIsCurrent(boolean flag);

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#store
     */
    public void store() throws IDOStoreException;

    /**
     * @see se.idega.idegaweb.commune.accounting.invoice.data.BatchDeadlineBMPBean#addBatch
     */
    public void addBatch(BatchRun batchRun) throws IDOAddRelationshipException;

}
