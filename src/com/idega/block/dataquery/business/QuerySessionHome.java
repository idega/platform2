package com.idega.block.dataquery.business;


public interface QuerySessionHome extends com.idega.business.IBOHome
{
 public QuerySession create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}