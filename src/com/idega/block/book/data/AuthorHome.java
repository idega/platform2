package com.idega.block.book.data;


public interface AuthorHome extends com.idega.data.IDOHome
{
 public Author create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Author findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllAuthors()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllAuthorsByBook(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}