package com.idega.block.book.data;


public interface Review extends com.idega.data.IDOEntity
{
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setRating(int p0) throws java.rmi.RemoteException;
 public void setBookID(int p0) throws java.rmi.RemoteException;
 public void setDateAdded(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public java.lang.String getReview() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setReview(java.lang.String p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getDateAdded() throws java.rmi.RemoteException;
 public java.lang.String getIDColumnName() throws java.rmi.RemoteException;
 public int getRating() throws java.rmi.RemoteException;
 public int getBookID() throws java.rmi.RemoteException;
}
