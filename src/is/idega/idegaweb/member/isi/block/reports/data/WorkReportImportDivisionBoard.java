package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportImportDivisionBoard extends com.idega.data.IDOEntity
{
 public java.lang.String getEmail();
 public java.lang.String getFax();
 public java.lang.String getFirstPhone();
 public int getGroupId();
 public java.lang.String getHomePage();
 public is.idega.idegaweb.member.isi.block.reports.data.WorkReportGroup getLeague()throws com.idega.data.IDOException;
 public int getNumberOfCompetitors();
 public int getNumberOfPlayers();
 public java.lang.String getPersonalId();
 public com.idega.core.location.data.PostalCode getPostalCode()throws java.sql.SQLException;
 public int getPostalCodeID();
 public int getReportId();
 public java.lang.String getSecondPhone();
 public java.lang.String getStreetName();
 public int getWorkReportGroupID();
 public boolean hasNationalLeague();
 public void initializeAttributes();
 public void setEmail(java.lang.String p0);
 public void setFax(java.lang.String p0);
 public void setFirstPhone(java.lang.String p0);
 public void setGroupId(int p0);
 public void setHasNationalLeague(boolean p0);
 public void setHomePage(java.lang.String p0);
 public void setLeague(int p0);
 public void setNumberOfCompetitors(int p0);
 public void setNumberOfPlayers(int p0);
 public void setPersonalId(java.lang.String p0);
 public void setPostalCode(com.idega.core.location.data.PostalCode p0);
 public void setPostalCodeID(int p0);
 public void setReportId(int p0);
 public void setSecondPhone(java.lang.String p0);
 public void setStreetName(java.lang.String p0);
 public void setWorKReportGroupID(int p0);
}
