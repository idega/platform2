package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingParameters extends com.idega.data.IDOEntity
{
 public com.idega.block.school.data.SchoolType getActivity()throws javax.ejb.FinderException;
 public java.sql.Timestamp getChangedDate();
 public java.lang.String getChangedSign();
 public se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType getCommuneBelonging()throws javax.ejb.FinderException;
 public com.idega.block.school.data.SchoolManagementType getCompanyType()throws javax.ejb.FinderException;
 public java.lang.String getDoublePostingString();
 public java.sql.Date getPeriodeFrom();
 public java.sql.Date getPeriodeTo();
 public java.lang.String getPostingString();
 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType getRegSpecType()throws javax.ejb.FinderException;
 public com.idega.block.school.data.SchoolYear getSchoolYear1()throws javax.ejb.FinderException;
 public com.idega.block.school.data.SchoolYear getSchoolYear2()throws javax.ejb.FinderException;
 public void initializeAttributes();
 public void setActivity(int p0);
 public void setChangedDate(java.sql.Timestamp p0);
 public void setChangedSign(java.lang.String p0);
 public void setCommuneBelonging(int p0);
 public void setCompanyType(java.lang.String p0);
 public void setDoublePostingString(java.lang.String p0);
 public void setPeriodeFrom(java.sql.Date p0);
 public void setPeriodeTo(java.sql.Date p0);
 public void setPostingString(java.lang.String p0);
 public void setRegSpecType(int p0);
 public void setSchoolYear1(int p0);
 public void setSchoolYear2(int p0);
}
