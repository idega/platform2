package se.idega.idegaweb.commune.accounting.invoice.data;


public interface BatchRun extends com.idega.data.IDOEntity
{
 public java.sql.Date getEnd();
 public java.sql.Date getPeriod();
 public java.lang.String getSchoolCategoryID();
 public java.sql.Date getStart();
 public void initializeAttributes();
 public void setEnd(java.sql.Date p0);
 public void setPeriod(java.sql.Date p0);
 public void setSchoolCategoryID(java.lang.String p0);
 public void setSchoolCategoryID(com.idega.block.school.data.SchoolCategory p0);
 public void setStart(java.sql.Date p0);
}
