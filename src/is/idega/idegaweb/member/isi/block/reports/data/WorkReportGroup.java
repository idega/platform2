package is.idega.idegaweb.member.isi.block.reports.data;


public interface WorkReportGroup extends com.idega.data.IDOEntity
{
 public void addEntity(com.idega.data.IDOEntity p0)throws com.idega.data.IDOAddRelationshipException;
 public java.lang.String getEmail();
 public java.util.Collection getMembers()throws com.idega.data.IDOException;
 public java.lang.String getFax();
 public java.lang.Integer getGroupId();
 public java.lang.String getGroupType();
 public java.lang.String getHomePhone();
 public java.lang.String getName();
 public java.lang.String getNumber();
 public java.lang.String getPersonalId();
 public com.idega.core.location.data.PostalCode getPostalCode()throws java.sql.SQLException;
 public int getPostalCodeID();
 public java.lang.String getShortName();
 public java.lang.String getStreetName();
 public java.lang.String getWorkPhone();
 public java.lang.Integer getYearOfReport();
 public void initializeAttributes();
 public void removeEntity(com.idega.data.IDOEntity p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setEmail(java.lang.String p0);
 public void setFax(java.lang.String p0);
 public void setGroupId(int p0);
 public void setGroupType(java.lang.String p0);
 public void setHomePhone(java.lang.String p0);
 public void setName(java.lang.String p0);
 public void setNumber(java.lang.String p0);
 public void setPersonalId(java.lang.String p0);
 public void setPostalCode(com.idega.core.location.data.PostalCode p0);
 public void setPostalCodeID(int p0);
 public void setShortName(java.lang.String p0);
 public void setStreetName(java.lang.String p0);
 public void setWorkPhone(java.lang.String p0);
 public void setYearOfReport(int p0);
}
