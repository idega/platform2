package com.idega.block.text.data;


public interface LocalizedText extends com.idega.data.IDOLegacyEntity
{
 public java.lang.String getBody();
 public java.lang.String getMarkupLanguage();
 public java.sql.Timestamp getCreated();
 public java.lang.String getHeadline();
 public int getLocaleId();
 public java.lang.String getTitle();
 public java.sql.Timestamp getUpdated();
 public void setBody(java.lang.String p0);
 public void setCreated(java.sql.Timestamp p0);
 public void setHeadline(java.lang.String p0);
 public void setMarkupLanguage(java.lang.String p0);
 public void setLocaleId(java.lang.Integer p0);
 public void setLocaleId(int p0);
 public void setTitle(java.lang.String p0);
 public void setUpdated(java.sql.Timestamp p0);
 public java.util.Collection ejbFindRelatedEntities(com.idega.data.IDOEntity entity) throws com.idega.data.IDORelationshipException;
 public void idoAddTo(com.idega.data.IDOEntity entity) throws com.idega.data.IDOAddRelationshipException;
 public void idoAddTo(String middleTableName, String sqlFieldName, Object primaryKey) throws com.idega.data.IDOAddRelationshipException; 
 	 
}
