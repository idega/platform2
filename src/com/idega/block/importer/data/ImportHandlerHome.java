package com.idega.block.importer.data;


public interface ImportHandlerHome extends com.idega.data.IDOHome
{
 public ImportHandler create() throws javax.ejb.CreateException;
 public ImportHandler findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAllAutomaticUpdates()throws javax.ejb.FinderException;
 public java.util.Collection findAllImportHandlers()throws javax.ejb.FinderException;
 public ImportHandler findByClassName(java.lang.String p0)throws javax.ejb.FinderException;

}