package com.idega.block.dataquery.business;


public interface QueryService extends com.idega.business.IBOService
{
 public com.idega.block.dataquery.business.QueryHelper getQueryHelper(com.idega.util.xml.XMLFile p0) throws java.rmi.RemoteException;
 public com.idega.block.dataquery.business.QueryHelper getQueryHelper() throws java.rmi.RemoteException;
 public com.idega.block.dataquery.business.QueryHelper getQueryHelper(int p0) throws java.rmi.RemoteException;
}
