package com.idega.block.finance.data;

import java.sql.Timestamp;

import com.idega.block.category.data.CategoryEntity;

public interface EntryGroup extends com.idega.data.IDOEntity, CategoryEntity {

	public int getEntryIdFrom();

	public int getEntryIdTo();

	public java.lang.String getFileName();

	public java.sql.Date getGroupDate();

	public int getGroupTypeId();

	public java.lang.String getInfo();

	public java.lang.String getName();

	public void setEntryIdFrom(int p0);

	public void setEntryIdTo(int p0);

	public void setFileName(java.lang.String p0);

	public void setGroupDate(java.sql.Date p0);

	public void setGroupTypeId(int p0);

	public void setInfo(java.lang.String p0);

	public Timestamp getFileInvoiceDate();

	public void setFileInvoiceDate(Timestamp invoiceDate);

	public Timestamp getFileDueDate();

	public void setFileDueDate(Timestamp dueDate);
}
