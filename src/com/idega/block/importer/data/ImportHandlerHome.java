package com.idega.block.importer.data;


public interface ImportHandlerHome extends com.idega.data.IDOHome
{
 public ImportHandler create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public ImportHandler findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllImportHandlers()throws javax.ejb.FinderException, java.rmi.RemoteException;
 
}