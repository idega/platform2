/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.creditcard.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author palli
 */
public class BatchBMPBean extends GenericEntity implements Batch {

    protected final static String ENTITY_NAME = "isi_creditcard_batch";

    protected final static String COLUMN_BATCH_NUMBER = "batch_number";
    
    protected final static String COLUMN_CREATED = "created";

    protected final static String COLUMN_CREDITCARD_TYPE = "card_type_id";

    protected final static String COLUMN_CONTRACT = "contract_number";
    
    protected final static String COLUMN_FILE_NAME = "file_name";
    
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
        addManyToOneRelationship(COLUMN_CREDITCARD_TYPE, CreditCardType.class);
        addAttribute(COLUMN_CONTRACT, "Contract number", true, true, String.class, 255);
        addAttribute(COLUMN_FILE_NAME, "File name", true, true, String.class, 255);
        addAttribute(COLUMN_SENT, "Sent", true, true, Timestamp.class);
    }

    //Setters
    public void setBatchNumber(String batchNumber) {
        setColumn(COLUMN_BATCH_NUMBER, batchNumber);
    }
    
    public void setCreated(Timestamp created) {
        setColumn(COLUMN_CREATED, created);
    }
    
    public void setCreditcardTypeID(int typeID) {
        setColumn(COLUMN_CREDITCARD_TYPE, typeID);
    }
    
    public void setCreditCardType(CreditCardType type) {
        setColumn(COLUMN_CREDITCARD_TYPE, type);
    }
    
    public void setContract(String contract) {
        setColumn(COLUMN_CONTRACT, contract);
    }
    
    public void setFileName(String fileName) {
        setColumn(COLUMN_FILE_NAME, fileName);
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
    
    public int getCreditcardTypeID() {
        return getIntColumnValue(COLUMN_CREDITCARD_TYPE);
    }
    
    public CreditCardType getCreditCardType() {
        return (CreditCardType) getColumnValue(COLUMN_CREDITCARD_TYPE);
    }
    
    public String getContract() {
        return getStringColumnValue(COLUMN_CONTRACT);
    }
    
    public String getFileName() {
        return getStringColumnValue(COLUMN_FILE_NAME);
    }
    
    public Timestamp getSent() {
        return getTimestampColumnValue(COLUMN_SENT);
    }
    
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		return idoFindPKsBySQL(sql.toString());
	}
	
	public Object ejbFindUnsentByContractNumberAndType(String contractNumber, CreditCardType type) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEqualsWithSingleQuotes(COLUMN_CONTRACT, contractNumber);
		sql.appendAnd();
		sql.append(COLUMN_SENT);
		sql.append(" is null");
		sql.appendAnd();
		sql.appendEquals(COLUMN_CREDITCARD_TYPE, type);
		
		return idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindAllNewestFirst() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderByDescending(getIDColumnName());

		return idoFindPKsBySQL(sql.toString());
	}

}