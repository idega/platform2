package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingParameters extends com.idega.data.IDOEntity
{
 public se.idega.idegaweb.commune.accounting.regulations.data.ActivityType getActivity();
 public java.sql.Timestamp getChangedDate();
 public java.lang.String getChangedSign();
 public se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType getCommuneBelonging();
 public se.idega.idegaweb.commune.accounting.regulations.data.CompanyType getCompanyType();
 public java.lang.String getDoublePostingString();
 public java.sql.Date getPeriodeFrom();
 public java.sql.Date getPeriodeTo();
 public java.lang.String getPostingString();
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType getRegSpecType();
 public void initializeAttributes();
 public void setActivity(int p0);
 public void setChangedDate(java.sql.Timestamp p0);
 public void setChangedSign(java.lang.String p0);
 public void setCommuneBelonging(int p0);
 public void setCompanyType(int p0);
 public void setDoublePostingString(java.lang.String p0);
 public void setPeriodeFrom(java.sql.Date p0);
 public void setPeriodeTo(java.sql.Date p0);
 public void setPostingStriong(java.lang.String p0);
 public void setRegSpecType(int p0);
}
