package com.idega.block.dataquery.data;


public interface UserQuery extends com.idega.data.IDOEntity
{
 public boolean getDeleted();
 public com.idega.user.data.User getDeletedBy();
 public java.sql.Timestamp getDeletedWhen();
 public java.lang.String getName();
 public com.idega.user.data.Group getOwnership();
 public java.lang.String getPermisson();
 public com.idega.block.dataquery.data.QuerySequence getRoot();
 public int getRootID();
 public com.idega.core.file.data.ICFile getSource();
 public int getSourceID();
 public void setDeleted(boolean p0);
 public void setDeletedBy(com.idega.user.data.User p0);
 public void setDeletedWhen(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
 public void setOwnership(com.idega.user.data.Group p0);
 public void setPermission(java.lang.String p0);
 public void setRoot(com.idega.block.dataquery.data.QuerySequence p0);
 public void setRootID(int p0);
 public void setSource(com.idega.core.file.data.ICFile p0);
 public void setSourceID(int p0);
}
