package com.idega.block.book.data;


public interface PublisherHome extends com.idega.data.IDOHome
{
 public Publisher create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Publisher findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllPublishers()throws javax.ejb.FinderException, java.rmi.RemoteException;

}