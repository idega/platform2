package com.idega.block.school.data;


public interface SchoolUserPaymentHistory extends com.idega.data.IDOEntity
{
 public java.sql.Date getDate();
 public int getMaxPayment();
 public java.sql.Date getModificationDate();
 public int getSchoolUserPaymentID();
 public void initializeAttributes();
 public void setDate(java.sql.Date p0);
 public void setMaxPayment(int p0);
 public void setModificationDate(java.sql.Date p0);
 public void setSchoolUserPaymentID(int p0);
}
