package com.idega.block.documents.data;


public interface DocVersion extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public java.sql.Timestamp getCreationDate();
 public int getFileID();
 public java.lang.String getIDColumnName();
 public int getPageID();
 public int getUserID()throws java.sql.SQLException;
 public void setCreationDate(java.sql.Timestamp p0);
 public void setFileID(int p0);
 public void setPageID(int p0);
 public void setUser(com.idega.core.user.data.User p0);
}
