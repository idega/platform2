package com.idega.block.quote.data;


public interface QuoteEntityHome extends com.idega.data.IDOHome
{
 public QuoteEntity create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public QuoteEntity findByPrimaryKey(int id) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public QuoteEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllQuotesByLocale(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}