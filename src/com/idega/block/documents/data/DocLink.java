package com.idega.block.documents.data;


public interface DocLink extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public int getCategoryID();
 public java.sql.Timestamp getCreationDate();
 public int getFileID();
 public int getFolderID();
 public java.lang.String getIDColumnName();
 public java.lang.String getName();
 public int getPageID();
 public java.lang.String getTarget();
 public java.lang.String getURL();
 public int getUserGroupID();
 public void setCategoryID(int p0);
 public void setCreationDate(java.sql.Timestamp p0);
 public void setFileID(int p0);
 public void setFolderID(int p0);
 public void setName(java.lang.String p0);
 public void setPageID(int p0);
 public void setTarget(java.lang.String p0);
 public void setURL(java.lang.String p0);
 public void setUser(com.idega.core.user.data.User p0);
}
