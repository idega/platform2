package com.idega.block.importer.data;


public interface ImportFileRecordHome extends com.idega.data.IDOHome
{
 public ImportFileRecord create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public ImportFileRecord findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public ImportFileRecord findImportFileRecordFromNameAndSize(java.lang.String p0,java.lang.Integer p1)throws javax.ejb.FinderException, java.rmi.RemoteException;

}