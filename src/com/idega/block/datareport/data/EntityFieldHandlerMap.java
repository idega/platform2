package com.idega.block.datareport.data;


public interface EntityFieldHandlerMap extends com.idega.data.IDOEntity
{
 public java.lang.Object findByEntityAndField(java.lang.String p0,java.lang.String p1)throws javax.ejb.FinderException;
 public java.lang.String getDescription();
 public java.lang.String getEntity();
 public java.lang.String getField();
 public java.lang.String getHandler();
 public void initializeAttributes();
 public void setDescription(java.lang.String p0);
 public void setEntity(java.lang.String p0);
 public void setField(java.lang.String p0);
 public void setHandler(java.lang.String p0);
}
