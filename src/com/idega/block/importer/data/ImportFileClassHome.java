package com.idega.block.importer.data;


public interface ImportFileClassHome extends com.idega.data.IDOHome
{
 public ImportFileClass create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public ImportFileClass findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllImportFileClasses()throws javax.ejb.FinderException, java.rmi.RemoteException;

}