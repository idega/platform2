package com.idega.block.book.data;


public interface BookHome extends com.idega.data.IDOHome
{
 public Book create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Book findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBooksContaining(java.lang.String p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBooksByPublisher(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBooksByAuthor(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllNewestBooks(int[] p0,int p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBooksByCategory(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findNewestBookByCategory(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllBooksByYear(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getNumberOfBooks(int p0)throws com.idega.data.IDOException,javax.ejb.EJBException, java.rmi.RemoteException;

}