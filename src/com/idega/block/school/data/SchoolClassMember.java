package com.idega.block.school.data;


public interface SchoolClassMember extends com.idega.data.IDOEntity
{
 public int getClassMemberId();
 public boolean getHasCompensationByInvoice();
 public java.lang.String getInvoiceInterval();
 public java.lang.String getLanguage();
 public java.sql.Timestamp getLatestInvoiceDate();
 public boolean getNeedsSpecialAttention();
 public java.lang.String getNotes();
 public java.sql.Timestamp getRegisterDate();
 public int getRegistratorId();
 public java.sql.Timestamp getRemovedDate();
 public com.idega.block.school.data.SchoolClass getSchoolClass();
 public int getSchoolClassId();
 public boolean getSpeciallyPlaced();
 public com.idega.user.data.User getStudent();
 public void initializeAttributes();
 public void setClassMemberId(int p0);
 public void setHasCompensationByInvoice(boolean p0);
 public void setInvoiceInterval(java.lang.String p0);
 public void setLanguage(java.lang.String p0);
 public void setLatestInvoiceDate(java.sql.Timestamp p0);
 public void setNeedsSpecialAttention(boolean p0);
 public void setNotes(java.lang.String p0);
 public void setRegisterDate(java.sql.Timestamp p0);
 public void setRegistratorId(int p0);
 public void setRemovedDate(java.sql.Timestamp p0);
 public void setSchoolClassId(int p0);
 public void setSpeciallyPlaced(boolean p0);
}
