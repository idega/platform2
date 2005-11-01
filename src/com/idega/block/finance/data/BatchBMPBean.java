/* Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package com.idega.block.finance.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author palli
 */
public class BatchBMPBean extends GenericEntity implements Batch {

    protected final static String ENTITY_NAME = "fin_batch";

    protected final static String COLUMN_BATCH_NUMBER = "batch_number";
    
    protected final static String COLUMN_CREATED = "created";

    protected final static String COLUMN_SENT = "sent";

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.data.GenericEntity#getEntityName()
     */
    public String getEntityName() {
        return ENTITY_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.idega.data.GenericEntity#initializeAttributes()
     */
    public void initializeAttributes() {
        addAttribute(getIDColumnName());
        addAttribute(COLUMN_BATCH_NUMBER, "Batch number", true, true, String.class, 255);
        addAttribute(COLUMN_CREATED, "Created", true, true, Timestamp.class);
        addAttribute(COLUMN_SENT, "Sent", true, true, Timestamp.class);
    }

    //Setters
    public void setBatchNumber(String batchNumber) {
        setColumn(COLUMN_BATCH_NUMBER, batchNumber);
    }
    
    public void setCreated(Timestamp created) {
        setColumn(COLUMN_CREATED, created);
    }
    
    public void setSent(Timestamp sent) {
        setColumn(COLUMN_SENT, sent);
    }
    
    //Getters
    public String getBatchNumber() {
        return getStringColumnValue(COLUMN_BATCH_NUMBER);
    }
    
    public Timestamp getCreated() {
        return getTimestampColumnValue(COLUMN_CREATED);
    }
    
    public Timestamp getSent() {
        return getTimestampColumnValue(COLUMN_SENT);
    }
    
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		return idoFindPKsBySQL(sql.toString());
	}
	
	public Object ejbFindUnsent() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_SENT);
		sql.append(" is null");
		
		return idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindAllNewestFirst() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderByDescending(getIDColumnName());

		return idoFindPKsBySQL(sql.toString());
	}
}