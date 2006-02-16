/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */

package is.idega.idegaweb.member.isi.block.accounting.export.data;

import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardContract;
import is.idega.idegaweb.member.isi.block.accounting.data.CreditCardType;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.finance.data.BankInfo;
import com.idega.core.file.data.ICFile;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author palli
 */
public class BatchBMPBean extends GenericEntity implements Batch {

	protected final static String ENTITY_NAME = "isi_batch";

	protected final static String COLUMN_BATCH_NUMBER = "batch_number";

	protected final static String COLUMN_BATCH_TYPE = "batch_type";

	protected final static String COLUMN_CREATED = "created";

	protected final static String COLUMN_SENT = "sent";
	
	protected final static String COLUMN_FIN_BATCH = "fin_batch_id";

	// For the creditcard batches
	protected final static String COLUMN_CC_TYPE = "cc_card_type_id";

	protected final static String COLUMN_CC_CONTRACT = "cc_contract_id";

	protected final static String COLUMN_CC_FILE_NAME = "cc_file_name";
	
	protected final static String COLUMN_CC_FILE = "cc_file_id";

	// For the bank batches
	protected final static String COLUMN_BANK_INFO = "bank_info_id";

	private final static String TYPE_BANK_BATCH = "B";
	
	private final static String TYPE_CREDITCARD_BATCH = "C";

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
		addAttribute(COLUMN_BATCH_NUMBER, "Batch number", true, true,
				String.class, 255);
		addAttribute(COLUMN_CREATED, "Created", true, true, Timestamp.class);
		addAttribute(COLUMN_SENT, "Sent", true, true, Timestamp.class);
		addAttribute(COLUMN_BATCH_TYPE, "Type", true, true, String.class, 1);

		addManyToOneRelationship(COLUMN_CC_TYPE, CreditCardType.class);
		addManyToOneRelationship(COLUMN_CC_CONTRACT, CreditCardContract.class);
		addAttribute(COLUMN_CC_FILE_NAME, "File name", true, true,
				String.class, 255);
		addManyToOneRelationship(COLUMN_CC_FILE, ICFile.class);

		addManyToOneRelationship(COLUMN_BANK_INFO, BankInfo.class);
		addOneToOneRelationship(COLUMN_FIN_BATCH, com.idega.block.finance.data.Batch.class);
	}

	// Setters
	public void setBatchNumber(String batchNumber) {
		setColumn(COLUMN_BATCH_NUMBER, batchNumber);
	}

	public void setCreated(Timestamp created) {
		setColumn(COLUMN_CREATED, created);
	}

	public void setSent(Timestamp sent) {
		setColumn(COLUMN_SENT, sent);
	}
	
	public void setTypeBank() {
		setColumn(COLUMN_BATCH_TYPE, TYPE_BANK_BATCH);
	}

	public void setTypeCreditCard() {
		setColumn(COLUMN_BATCH_TYPE, TYPE_CREDITCARD_BATCH);
	}
	
	public void setCreditcardTypeID(int typeID) {
		setColumn(COLUMN_CC_TYPE, typeID);
	}

	public void setCreditCardType(CreditCardType type) {
		setColumn(COLUMN_CC_TYPE, type);
	}

	public void setCreditCardContractId(int contractID) {
		setColumn(COLUMN_CC_CONTRACT, contractID);
	}

	public void setCreditCardContract(CreditCardContract contract) {
		setColumn(COLUMN_CC_CONTRACT, contract);
	}

	public void setCreditCardFileName(String fileName) {
		setColumn(COLUMN_CC_FILE_NAME, fileName);
	}
	
	public void setCreditCardFile(ICFile file) {
		setColumn(COLUMN_CC_FILE, file);
	}

	public void setCreditCardFileId(int fileId) {
		setColumn(COLUMN_CC_FILE, fileId);
	}

	public void setBankInfoID(int infoID) {
		setColumn(COLUMN_BANK_INFO, infoID);
	}

	public void setBankInfo(BankInfo info) {
		setColumn(COLUMN_BANK_INFO, info);
	}
	
	public void setFinBatchID(int batchID) {
		setColumn(COLUMN_FIN_BATCH, batchID);
	}
	
	public void setFinBatch(com.idega.block.finance.data.Batch batch) {
		setColumn(COLUMN_FIN_BATCH, batch);
	}

	// Getters
	public String getBatchNumber() {
		return getStringColumnValue(COLUMN_BATCH_NUMBER);
	}

	public Timestamp getCreated() {
		return getTimestampColumnValue(COLUMN_CREATED);
	}

	public Timestamp getSent() {
		return getTimestampColumnValue(COLUMN_SENT);
	}
	
	public String getType() {
		return getStringColumnValue(COLUMN_BATCH_TYPE);
	}

	public boolean getIsBankType() {
		return getStringColumnValue(COLUMN_BATCH_TYPE).equals(TYPE_BANK_BATCH);
	}

	public boolean getIsCreditcardType() {
		return getStringColumnValue(COLUMN_BATCH_TYPE).equals(TYPE_CREDITCARD_BATCH);
	}
	
	public int getCreditcardTypeID() {
		return getIntColumnValue(COLUMN_CC_TYPE);
	}

	public CreditCardType getCreditCardType() {
		return (CreditCardType) getColumnValue(COLUMN_CC_TYPE);
	}

	public int getCreditCardContractID() {
		return getIntColumnValue(COLUMN_CC_CONTRACT);
	}

	public CreditCardContract getCreditCardContract() {
		return (CreditCardContract) getColumnValue(COLUMN_CC_CONTRACT);
	}

	public String getCreditCardFileName() {
		return getStringColumnValue(COLUMN_CC_FILE_NAME);
	}

	public ICFile getCreditCardFile() {
		return (ICFile) getColumnValue(COLUMN_CC_FILE);
	}

	public int getCreditCardFileId() {
		return getIntColumnValue(COLUMN_CC_FILE);
	}

	public int getBankInfoID() {
		return getIntColumnValue(COLUMN_BANK_INFO);
	}
	
	public BankInfo getBankInfo() {
		return (BankInfo) getColumnValue(COLUMN_BANK_INFO);
	}
	
	public int getFinBatchID() {
		return getIntColumnValue(COLUMN_FIN_BATCH);
	}
	
	public com.idega.block.finance.data.Batch getFinBatch() {
		return (com.idega.block.finance.data.Batch) getColumnValue(COLUMN_FIN_BATCH);
	}

	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindUnsentByContractAndCreditCardType(CreditCardContract contract,
			CreditCardType type) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_CC_CONTRACT, contract);
		sql.appendAnd();
		sql.append(COLUMN_SENT);
		sql.append(" is null");
		sql.appendAnd();
		sql.appendEquals(COLUMN_CC_TYPE, type);

		System.out.println("sql = " + sql.toString());

		return idoFindOnePKByQuery(sql);
	}

	public Collection ejbFindAllUnsent() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.append(COLUMN_SENT);
		sql.append(" is null");

		return idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllWithoutFiles() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere();
		sql.append(COLUMN_SENT);
		sql.append(" is null");
		sql.appendAnd();
		sql.append(COLUMN_FIN_BATCH);
		sql.append(" is null");
		
		return idoFindPKsByQuery(sql);
	}
	
	public Object ejbFindUnsentByBankInfo(BankInfo info) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(COLUMN_BANK_INFO, info);
		sql.appendAnd();
		sql.append(COLUMN_SENT);
		sql.append(" is null");

		System.out.println("sql = " + sql.toString());

		return idoFindOnePKByQuery(sql);
	}
	
	public Collection ejbFindAllNewestFirst() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderByDescending(getIDColumnName());

		return idoFindPKsByQuery(sql);
	}
}