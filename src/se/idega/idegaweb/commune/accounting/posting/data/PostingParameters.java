package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingParameters extends com.idega.data.IDOEntity
{
 public se.idega.idegaweb.commune.accounting.regulations.data.ActivityType getActivity();
 public java.sql.Timestamp getChangedDate();
 public java.lang.String getChangedSign();
 public se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType getCommuneBelonging();
 public se.idega.idegaweb.commune.accounting.regulations.data.CompanyType getCompanyType();
 public java.lang.String getDoublePostingAccount();
 public java.lang.String getDoublePostingActivity();
 public java.lang.String getDoublePostingActivityCode();
 public java.lang.String getDoublePostingDoubleEntry();
 public java.lang.String getDoublePostingLiability();
 public java.lang.String getDoublePostingObject();
 public java.lang.String getDoublePostingProject();
 public java.lang.String getDoublePostingResource();
 public java.sql.Date getPeriodeFrom();
 public java.sql.Date getPeriodeTo();
 public java.lang.String getPostingAccount();
 public java.lang.String getPostingActivity();
 public java.lang.String getPostingActivityCode();
 public java.lang.String getPostingDoubleEntry();
 public java.lang.String getPostingLiability();
 public java.lang.String getPostingObject();
 public java.lang.String getPostingProject();
 public java.lang.String getPostingResource();
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType getRegSpecType();
 public void initializeAttributes();
 public void setActivity(int p0);
 public void setChangedDate(java.sql.Timestamp p0);
 public void setChangedSign(java.lang.String p0);
 public void setCommuneBelonging(int p0);
 public void setCompanyType(int p0);
 public void setDoublePostingAccount(java.lang.String p0);
 public void setDoublePostingActivity(java.lang.String p0);
 public void setDoublePostingActivityCode(java.lang.String p0);
 public void setDoublePostingDoubleEntry(java.lang.String p0);
 public void setDoublePostingLiability(java.lang.String p0);
 public void setDoublePostingObject(java.lang.String p0);
 public void setDoublePostingProject(java.lang.String p0);
 public void setDoublePostingResource(java.lang.String p0);
 public void setPeriodeFrom(java.sql.Date p0);
 public void setPeriodeTo(java.sql.Date p0);
 public void setPostingAccount(java.lang.String p0);
 public void setPostingActivity(java.lang.String p0);
 public void setPostingActivityCode(java.lang.String p0);
 public void setPostingDoubleEntry(java.lang.String p0);
 public void setPostingLiability(java.lang.String p0);
 public void setPostingObject(java.lang.String p0);
 public void setPostingProject(java.lang.String p0);
 public void setPostingResource(java.lang.String p0);
 public void setRegSpecType(int p0);
}
