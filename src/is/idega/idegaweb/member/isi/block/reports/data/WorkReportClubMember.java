package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportClubMember extends com.idega.data.IDOEntity
{
 public int getAge();
 public java.sql.Timestamp getDateOfBirth();
 public java.util.Collection getLeaguesForMember()throws com.idega.data.IDOException;
 public java.lang.String getName();
 public java.lang.String getPersonalId();
 public com.idega.core.data.PostalCode getPostalCode()throws java.sql.SQLException;
 public int getPostalCodeID();
 public int getReportId();
 public java.lang.String getStreetName();
 public int getUserId();
 public void initializeAttributes();
 public boolean isBoardMember();
 public boolean isFemale();
 public boolean isMale();
 public void setAge(int p0);
 public void setAsBoardMember(boolean p0);
 public void setAsFemale();
 public void setAsMale();
 public void setDateOfBirth(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
 public void setPersonalId(java.lang.String p0);
 public void setPostalCode(com.idega.core.data.PostalCode p0);
 public void setPostalCodeID(int p0);
 public void setReportId(int p0);
 public void setStreetName(java.lang.String p0);
 public void setUserId(int p0);
}
