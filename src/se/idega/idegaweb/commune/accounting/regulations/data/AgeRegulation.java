package se.idega.idegaweb.commune.accounting.regulations.data;


public interface AgeRegulation extends com.idega.data.IDOEntity
{
 public int getAgeFrom();
 public java.lang.String getAgeInterval();
 public int getAgeTo();
 public java.sql.Date getCutDate();
 public java.lang.String getDescription();
 public java.lang.String getIDColumnName();
 public java.sql.Date getPeriodFrom();
 public java.sql.Date getPeriodTo();
 public void initializeAttributes();
 public void setAgeFrom(int p0);
 public void setAgeTo(int p0);
 public void setCutDate(java.sql.Date p0);
 public void setDescription(java.lang.String p0);
 public void setPeriodFrom(java.sql.Date p0);
 public void setPeriodTo(java.sql.Date p0);
}
