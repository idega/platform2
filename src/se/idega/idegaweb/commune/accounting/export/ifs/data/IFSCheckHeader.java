package se.idega.idegaweb.commune.accounting.export.ifs.data;


public interface IFSCheckHeader extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getEventDate();
 public java.sql.Timestamp getEventEndTime();
 public java.sql.Timestamp getEventStartTime();
 public com.idega.block.school.data.SchoolCategory getSchoolCategory();
 public java.lang.String getSchoolCategoryString();
 public java.lang.String getStatus();
 public java.lang.String getStatusFileCreated();
 public void setEventDate(java.sql.Timestamp p0);
 public void setEventEndTime(java.sql.Timestamp p0);
 public void setEventStartTime(java.sql.Timestamp p0);
 public void setSchoolCategory(com.idega.block.school.data.SchoolCategory p0);
 public void setSchoolCategoryString(java.lang.String p0);
 public void setStatus(java.lang.String p0);
 public void setStatusFileCreated();
}
