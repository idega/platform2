package com.idega.block.trade.stockroom.data;


public interface ProductHome extends com.idega.data.IDOHome
{
 public Product create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Product findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProducts(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProducts(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;

}