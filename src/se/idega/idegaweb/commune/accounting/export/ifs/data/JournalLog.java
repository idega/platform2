package se.idega.idegaweb.commune.accounting.export.ifs.data;


public interface JournalLog extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getEventDate();
 public java.lang.String getEventFileCreated();
 public java.lang.String getEventFileDeleted();
 public java.lang.String getEventFileSent();
 public java.lang.String getLocalizedEventKey();
 public com.idega.block.school.data.SchoolCategory getSchoolCategory();
 public java.lang.String getSchoolCategoryString();
 public com.idega.user.data.User getUser();
 public int getUserId();
 public void initializeAttributes();
 public void setEventDate(java.sql.Timestamp p0);
 public void setEventFileCreated();
 public void setEventFileDeleted();
 public void setEventFileSent();
 public void setLocalizedEventKey(java.lang.String p0);
 public void setSchoolCategory(com.idega.block.school.data.SchoolCategory p0);
 public void setSchoolCategoryString(java.lang.String p0);
 public void setUser(com.idega.user.data.User p0);
 public void setUserId(int p0);
}
