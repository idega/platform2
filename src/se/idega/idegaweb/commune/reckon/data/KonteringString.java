package se.idega.idegaweb.commune.reckon.data;


public interface KonteringString extends com.idega.data.IDOEntity
{
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void initializeAttributes();
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
