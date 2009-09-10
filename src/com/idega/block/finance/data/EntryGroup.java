/**
 * 
 */
package com.idega.block.finance.data;

import java.sql.Timestamp;

import com.idega.block.category.data.CategoryEntity;
import com.idega.core.file.data.ICFile;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface EntryGroup extends IDOEntity, CategoryEntity {
	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getName
	 */
	public String getName();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getGroupTypeId
	 */
	public int getGroupTypeId();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setGroupTypeId
	 */
	public void setGroupTypeId(int id);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getEntryIdFrom
	 */
	public int getEntryIdFrom();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setEntryIdFrom
	 */
	public void setEntryIdFrom(int id);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getEntryIdTo
	 */
	public int getEntryIdTo();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setEntryIdTo
	 */
	public void setEntryIdTo(int id);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getFileName
	 */
	public String getFileName();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setFileName
	 */
	public void setFileName(String fileName);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getInfo
	 */
	public String getInfo();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setInfo
	 */
	public void setInfo(String info);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getGroupDate
	 */
	public java.sql.Date getGroupDate();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setGroupDate
	 */
	public void setGroupDate(java.sql.Date date);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getFileInvoiceDate
	 */
	public Timestamp getFileInvoiceDate();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setFileInvoiceDate
	 */
	public void setFileInvoiceDate(Timestamp invoiceDate);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getFileDueDate
	 */
	public Timestamp getFileDueDate();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setFileDueDate
	 */
	public void setFileDueDate(Timestamp dueDate);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setFileId
	 */
	public void setFileId(int fileId);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getFileId
	 */
	public int getFileId();

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#setFile
	 */
	public void setFile(ICFile file);

	/**
	 * @see com.idega.block.finance.data.EntryGroupBMPBean#getFile
	 */
	public ICFile getFile();

}
