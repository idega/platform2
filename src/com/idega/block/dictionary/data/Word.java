package com.idega.block.dictionary.data;

import javax.ejb.*;

public interface Word extends com.idega.data.IDOEntity
{
 public java.lang.String getWord() throws java.rmi.RemoteException;
 public int getCategoryID() throws java.rmi.RemoteException;
 public void setCategoryID(int p0) throws java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public void setWord(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public java.lang.String getIDColumnName() throws java.rmi.RemoteException;
}
