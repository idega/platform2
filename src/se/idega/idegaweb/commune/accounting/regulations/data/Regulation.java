package se.idega.idegaweb.commune.accounting.regulations.data;


public interface Regulation extends com.idega.data.IDOEntity
{
 public java.lang.String getIDColumnName();
 public java.sql.Date getPeriodFrom();
 public java.sql.Date getPeriodTo();
 public void initializeAttributes();
 public void setPeriodFrom(java.sql.Date p0);
 public void setPeriodTo(java.sql.Date p0);
}
