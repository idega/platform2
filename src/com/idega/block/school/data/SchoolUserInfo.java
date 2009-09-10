package com.idega.block.school.data;


public interface SchoolUserInfo extends com.idega.data.IDOEntity
{
 public int getChildCareAge();
 public int getInvoiceReceiver();
 public int getSiblingNumber();
 public int getUserID();
 public void initializeAttributes();
 public void setChildCareAge(int p0);
 public void setInvoiceReceiver(int p0);
 public void setSiblingNumber(int p0);
 public void setUserID(int p0);
}
