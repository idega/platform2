package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportDivisionBoard extends com.idega.data.IDOEntity
{
 public java.lang.String getEmail();
 public java.lang.String getFax();
 public java.lang.String getFirstPhone();
 public int getGroupId();
 public java.lang.String getHomePage();
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup getLeague()throws com.idega.data.IDOException;
 public java.lang.String getPersonalId();
 public com.idega.core.data.PostalCode getPostalCode()throws java.sql.SQLException;
 public int getPostalCodeID();
 public int getReportId();
 public java.lang.String getSecondPhone();
 public java.lang.String getStreetName();
 public int getWorkReportGroupID();
 public void initializeAttributes();
 public void setEmail(java.lang.String p0);
 public void setFax(java.lang.String p0);
 public void setFirstPhone(java.lang.String p0);
 public void setGroupId(int p0);
 public void setHomePage(java.lang.String p0);
 public void setLeague(int p0);
 public void setPersonalId(java.lang.String p0);
 public void setPostalCode(com.idega.core.data.PostalCode p0);
 public void setPostalCodeID(int p0);
 public void setReportId(int p0);
 public void setSecondPhone(java.lang.String p0);
 public void setStreetName(java.lang.String p0);
 public void setWorKReportGroupID(int p0);
}
