package com.idega.block.trade.stockroom.data;


public interface Variant extends com.idega.data.IDOEntity
{
 public java.lang.String getName() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setProductId(int p0) throws java.rmi.RemoteException;
 public void setProduct(com.idega.block.trade.stockroom.data.Product p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product getProduct()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getProductId() throws java.rmi.RemoteException;
}
