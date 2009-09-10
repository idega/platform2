package com.idega.block.school.data;


public interface SchoolUserPayment extends com.idega.data.IDOEntity
{
 public java.sql.Date getDate();
 public int getMaxPayment();
 public int getUserID();
 public void initializeAttributes();
 public void setDate(java.sql.Date p0);
 public void setMaxPayment(int p0);
 public void setUserID(int p0);
}
