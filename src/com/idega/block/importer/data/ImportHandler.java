package com.idega.block.importer.data;


public interface ImportHandler extends com.idega.data.IDOEntity
{
 public java.lang.String getAutoImpFileType();
 public java.lang.String getAutoImpFolder();
 public java.lang.String getClassName();
 public java.lang.String getDescription();
 public java.lang.String getName();
 public void setAutoImpFileType(java.lang.String p0);
 public void setAutoImpFolder(java.lang.String p0);
 public void setClassName(java.lang.String p0);
 public void setDescription(java.lang.String p0);
 public void setName(java.lang.String p0);
}
