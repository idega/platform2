package se.idega.idegaweb.commune.accounting.invoice.data;


public interface PaymentHeader extends com.idega.data.IDOEntity
{
 public java.sql.Date getDateAttested();
 public java.sql.Date getPeriod();
 public com.idega.block.school.data.School getSchool();
 public int getSchoolCategoryID();
 public int getSchoolID();
 public int getSignatureID();
 public char getStatus();
 public void initializeAttributes();
 public void setDateAttested(java.sql.Date p0);
 public void setDateChanged(java.sql.Date p0);
 public void setSchoolCategoryID(com.idega.block.school.data.SchoolCategory p0);
 public void setSchoolCategoryID(int p0);
 public void setSchoolID(int p0);
 public void setSchoolID(com.idega.block.school.data.School p0);
 public void setSignaturelID(com.idega.user.data.User p0);
 public void setSignaturelID(int p0);
 public void setStatus(char p0);
}
