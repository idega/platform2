package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingParameters extends com.idega.data.IDOEntity,com.idega.data.IDOLegacyEntity
{
 public java.lang.Integer getActivity();
 public java.lang.Integer getCommuneBelonging();
 public java.lang.Integer getCompanyType();
 public java.lang.String getDoubleEntry();
 public java.lang.String getOwnEntry();
 public java.lang.String getPeriodeFrom();
 public java.lang.String getPeriodeTo();
 public java.lang.Integer getRegSpecType();
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
