package com.idega.block.trade.stockroom.data;


public interface VariantValue extends com.idega.data.IDOEntity
{
 public com.idega.block.trade.stockroom.data.Variant getVariant()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void setVariantId(int p0) throws java.rmi.RemoteException;
 public void setValue(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getValue() throws java.rmi.RemoteException;
 public int getVariantId() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
}
