package com.idega.block.book.data;


public interface Book extends com.idega.data.IDOEntity
{
 public int getYear() throws java.rmi.RemoteException;
 public int getImage() throws java.rmi.RemoteException;
 public java.lang.String getIDColumnName() throws java.rmi.RemoteException;
 public void setPublisherID(int p0) throws java.rmi.RemoteException;
 public void addToCategory(com.idega.block.category.data.ICCategory p0)throws com.idega.data.IDOException, java.rmi.RemoteException;
 public void setDateAdded(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void removeFromCategory()throws com.idega.data.IDOException, java.rmi.RemoteException;
 public void removeFromAuthor()throws com.idega.data.IDOException, java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public com.idega.block.book.data.ReviewHome getReviewHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public int getPublisherID() throws java.rmi.RemoteException;
 public void addToAuthor(com.idega.block.book.data.Author p0)throws com.idega.data.IDOException, java.rmi.RemoteException;
 public void setImageID(int p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getDateAdded() throws java.rmi.RemoteException;
 public void setYear(int p0) throws java.rmi.RemoteException;
 public java.util.Collection findRelatedCategories()throws com.idega.data.IDOException, java.rmi.RemoteException;

}
