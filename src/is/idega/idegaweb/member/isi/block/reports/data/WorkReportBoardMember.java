package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportBoardMember extends com.idega.data.IDOEntity
{
 public int getAge();
 public java.sql.Timestamp getDateOfBirth();
 public java.lang.String getEmail();
 public java.lang.String getFax();
 public java.lang.String getHomePhone();
 public java.lang.String getName();
 public java.lang.String getPersonalId();
 public com.idega.core.data.PostalCode getPostalCode()throws java.sql.SQLException;
 public int getPostalCodeID();
 public int getReportId();
 public java.lang.String getStatus();
 public java.lang.String getStreetName();
 public int getUserId();
 public java.lang.String getWorkPhone();
 public int getWorkReportGroupId();
 public void initializeAttributes();
 public boolean isFemale();
 public boolean isMale();
 public void setAge(int p0);
 public void setAsFemale();
 public void setAsMale();
 public void setDateOfBirth(java.sql.Timestamp p0);
 public void setEmail(java.lang.String p0);
 public void setFax(java.lang.String p0);
 public void setHomePhone(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setPersonalId(java.lang.String p0);
 public void setPostalCode(com.idega.core.data.PostalCode p0);
 public void setPostalCodeID(int p0);
 public void setReportId(int p0);
 public void setStatus(java.lang.String p0);
 public void setStreetName(java.lang.String p0);
 public void setUserId(int p0);
 public void setWorkPhone(java.lang.String p0);
 public void setWorkReportGroupId(int p0);
}
