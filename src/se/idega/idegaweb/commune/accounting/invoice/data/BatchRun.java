package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.util.CalendarMonth;


public interface BatchRun extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getEnd();
 public java.sql.Date getPeriod();
 public java.lang.String getSchoolCategoryID();
 public java.sql.Timestamp getStart();
 public boolean getTest();
 public void initializeAttributes();
 public void setEnd(java.sql.Timestamp p0);
 public void setPeriod(java.sql.Date p0);
 public void setSchoolCategoryID(com.idega.block.school.data.SchoolCategory p0);
 public void setSchoolCategoryID(java.lang.String p0);
 public void setStart(java.sql.Timestamp p0);
 public void setTest(boolean test);
 /**
  * Gets the CalendarMonth for the Period
  * @return
  */
 public CalendarMonth getMonth(); 
}
