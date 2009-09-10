package com.idega.block.book.data;

import javax.ejb.*;

public interface Author extends com.idega.data.IDOEntity
{
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setDateAdded(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void remove()throws com.idega.data.IDORemoveException, RemoveException;
 public void setImageID(int p0) throws java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getDateAdded() throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public java.lang.String getIDColumnName() throws java.rmi.RemoteException;
 public int getImage() throws java.rmi.RemoteException;
}
