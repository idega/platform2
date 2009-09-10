package com.idega.block.boxoffice.data;

import com.idega.core.file.data.ICFile;


public interface BoxLink extends com.idega.data.IDOLegacyEntity
{
 public void delete()throws java.sql.SQLException;
 public int getBoxCategoryID();
 public int getBoxID();
 public java.sql.Timestamp getCreationDate();
 public int getFileID();
 public ICFile getFile();
 public java.lang.String getIDColumnName();
 public int getPageID();
 public java.lang.String getTarget();
 public java.lang.String getURL();
 public int getUserID();
 public void setBoxCategoryID(int p0);
 public void setBoxID(int p0);
 public void setCreationDate(java.sql.Timestamp p0);
 public void setFileID(int p0);
 public void setPageID(int p0);
 public void setTarget(java.lang.String p0);
 public void setURL(java.lang.String p0);
 public void setUserID(int p0);
}
