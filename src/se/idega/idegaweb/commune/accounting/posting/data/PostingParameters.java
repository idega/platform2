package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingParameters extends com.idega.data.IDOEntity,com.idega.data.IDOLegacyEntity
{
 public se.idega.idegaweb.commune.accounting.regulations.data.ActivityType getActivity();
 public se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType getCommuneBelonging();
 public se.idega.idegaweb.commune.accounting.regulations.data.CompanyType getCompanyType();
 public java.lang.String getDoubleEntry();
 public java.lang.String getOwnEntry();
 public java.lang.String getPeriodeFrom();
 public java.lang.String getPeriodeTo();
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType getRegSpecType();
 public void initializeAttributes();
 public void setActivity(int p0);
 public void setCommuneBelonging(int p0);
 public void setCompanyType(int p0);
 public void setDoubleEntry(java.lang.String p0);
 public void setOwnEntry(java.lang.String p0);
 public void setPeriodeFrom(java.lang.String p0);
 public void setPeriodeTo(java.lang.String p0);
 public void setRegSpecType(int p0);
}
