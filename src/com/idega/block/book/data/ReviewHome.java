package com.idega.block.book.data;


public interface ReviewHome extends com.idega.data.IDOHome
{
 public Review create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Review findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllReviewsForBook(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findNewestReviewForBook(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getRatingTotal(int p0)throws com.idega.data.IDOException,javax.ejb.EJBException, java.rmi.RemoteException;
 public int getNumberOfReviews(int p0)throws com.idega.data.IDOException,javax.ejb.EJBException, java.rmi.RemoteException;

}