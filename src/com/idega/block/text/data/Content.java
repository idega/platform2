package com.idega.block.text.data;


public interface Content extends com.idega.data.IDOLegacyEntity
{
 public void addFileToContent(com.idega.core.file.data.ICFile p0)throws com.idega.data.IDOAddRelationshipException;
 public java.util.Collection getContentFiles()throws com.idega.data.IDORelationshipException;
 public java.sql.Timestamp getCreated();
 public java.sql.Timestamp getLastUpdated();
 public java.lang.String getLocalizedTextMiddleTableName(com.idega.data.IDOLegacyEntity p0,com.idega.data.IDOLegacyEntity p1);
 public java.sql.Timestamp getPublishFrom();
 public java.sql.Timestamp getPublishTo();
 public int getUserId();
 public void initializeAttributes();
 public void removeFileFromContent(com.idega.core.file.data.ICFile p0)throws com.idega.data.IDORemoveRelationshipException;
 public void setCreated(java.sql.Timestamp p0);
 public void setLastUpdated(java.sql.Timestamp p0);
 public void setPublishFrom(java.sql.Timestamp p0);
 public void setPublishTo(java.sql.Timestamp p0);
 public void setUserId(int p0);
 public void setUserId(java.lang.Integer p0);
}
