package com.idega.block.importer.data;


public interface ImportHandler extends com.idega.data.IDOEntity
{
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public void setClassName(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getClassName() throws java.rmi.RemoteException;
}
