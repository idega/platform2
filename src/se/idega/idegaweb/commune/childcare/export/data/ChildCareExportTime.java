package se.idega.idegaweb.commune.childcare.export.data;


public interface ChildCareExportTime extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getCreated();
 public java.lang.String getFileName();
 public int getFileType();
 public java.sql.Date getFromDate();
 public java.lang.String getIDColumnName();
 public java.sql.Date getToDate();
 public com.idega.user.data.User getUser();
 public int getUserId();
 public void initializeAttributes();
 public void setCreated(java.sql.Timestamp p0);
 public void setFileName(java.lang.String p0);
 public void setFileType(int p0);
 public void setFromDate(java.sql.Date p0);
 public void setToDate(java.sql.Date p0);
 public void setUserId(int p0);
}
