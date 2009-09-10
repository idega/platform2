package com.idega.block.finance.data;


import java.sql.Timestamp;
import com.idega.data.IDOEntity;

public interface Batch extends IDOEntity {
	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#setBatchNumber
	 */
	public void setBatchNumber(String batchNumber);

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#setCreated
	 */
	public void setCreated(Timestamp created);

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#setSent
	 */
	public void setSent(Timestamp sent);

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#setExternalBatchNumber
	 */
	public void setExternalBatchNumber(String number);

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#getBatchNumber
	 */
	public String getBatchNumber();

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#getCreated
	 */
	public Timestamp getCreated();

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#getSent
	 */
	public Timestamp getSent();

	/**
	 * @see com.idega.block.finance.data.BatchBMPBean#getExternalBatchNumber
	 */
	public String getExternalBatchNumber();
}