package se.idega.idegaweb.commune.accounting.invoice.data;

import java.sql.Date;


public interface RegularPaymentEntry extends com.idega.data.IDOEntity
{
	 public float getAmount();
	 public java.lang.String getDoublePosting();
	 public java.sql.Date getFrom();
	 public java.lang.String getNote();
	 public java.lang.String getOwnPosting();
	 public java.lang.String getPlacing();
	 public se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType getRegSpecType();
	 public int getRegSpecTypeId();
	 public com.idega.block.school.data.School getSchool();
	 public int getSchoolId();
	 public com.idega.block.school.data.SchoolType getSchoolType();
	 public int getSchoolTypeId();
	 public java.sql.Date getTo();
	 public com.idega.user.data.User getUser();
	 public int getUserId();
	 public float getVAT();
	 public se.idega.idegaweb.commune.accounting.regulations.data.Regulation getVatRuleRegulation();
	 public int getVatRuleRegulationId();
	public Date getCreatedDate();
	public String getCreatedName();
	public Date getEditDate();
	public String getEditName();
		 
	 public void setAmount(float p0);
	 public void setDoublePosting(java.lang.String p0);
	 public void setFrom(java.sql.Date p0);
	 public void setNote(java.lang.String p0);
	 public void setOwnPosting(java.lang.String p0);
	 public void setPlacing(java.lang.String p0);
	 public void setRegSpecType(se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType p0);
	 public void setRegSpecTypeId(int p0);
	 public void setSchoolId(int p0);
	 public void setSchoolTypeId(int p0);
	 public void setTo(java.sql.Date p0);
	 public void setUser(com.idega.user.data.User p0);
	 public void setVAT(float p0);
	 public void setVatRuleRegulation(se.idega.idegaweb.commune.accounting.regulations.data.Regulation p0);
	 public void setVatRuleRegulationId(int p0);
	public void setCreatedDate(java.sql.Date p0);
	public void setCreatedSign(java.lang.String p0);	
	public void setEditDate(java.sql.Date p0);
	public void setEditSign(java.lang.String p0);		 


}

