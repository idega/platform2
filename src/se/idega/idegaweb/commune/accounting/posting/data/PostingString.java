package se.idega.idegaweb.commune.accounting.posting.data;


public interface PostingString extends com.idega.data.IDOEntity,com.idega.data.IDOLegacyEntity
{
 public java.sql.Date getValidFrom();
 public java.sql.Date getValidTo();
 public void initializeAttributes();
 public void setValidFrom(java.sql.Date p0);
 public void setValidTo(java.sql.Date p0);
}
