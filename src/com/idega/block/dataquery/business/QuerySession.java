package com.idega.block.dataquery.business;


public interface QuerySession extends com.idega.business.IBOSession
{
 public void createNewQuery()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void createQuery(int p0,com.idega.presentation.IWContext p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException, java.io.IOException;
 public com.idega.block.dataquery.data.xml.QueryHelper getQueryHelper(com.idega.presentation.IWContext p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException,java.io.IOException;;
 public com.idega.block.dataquery.business.QueryService getQueryService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.core.file.data.ICFile getXMLFile(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setUserQueryID(int p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.data.UserQuery storeQuery(java.lang.String p0,boolean p1,boolean p2)throws com.idega.data.IDOStoreException,java.rmi.RemoteException,java.io.IOException,javax.ejb.CreateException,java.sql.SQLException,javax.ejb.FinderException, java.rmi.RemoteException;
}
