package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportClubMember extends com.idega.data.IDOEntity
{
 public int getAge();
 public java.sql.Timestamp getDateOfBirth();
 public java.lang.String getName();
 public java.lang.String getPersonalId();
 public int getReportId();
 public int getUserId();
 public int getWorkReportGroupId();
 public void initializeAttributes();
 public boolean isFemale();
 public boolean isMale();
 public void setAge(int p0);
 public void setAsFemale();
 public void setAsMale();
 public void setDateOfBirth(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
 public void setPersonalId(java.lang.String p0);
 public void setReportId(int p0);
 public void setUserId(int p0);
 public void setWorkReportGroupId(int p0);
}
