package com.idega.block.dataquery.business;


public interface QuerySession extends com.idega.business.IBOSession
{
 public void createNewQuery()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void createQuery(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.dataquery.business.QueryHelper getQueryHelper() throws java.rmi.RemoteException;
 public com.idega.block.dataquery.business.QueryService getQueryService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setXmlFileID(int p0) throws java.rmi.RemoteException;
 public com.idega.core.data.ICFile storeQuery(String name,int folderID)throws java.io.IOException, java.rmi.RemoteException;
 public com.idega.core.data.ICFile getXMLFile(int fileID)throws  java.rmi.RemoteException;
}
