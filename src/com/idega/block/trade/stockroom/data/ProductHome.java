package com.idega.block.trade.stockroom.data;
import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.util.IWTimestamp;
import java.util.Collection;


public interface ProductHome extends com.idega.data.IDOHome
{
 public Product create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Product findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProducts(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3,java.lang.String p4)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProducts(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProducts(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProductsOrderedByProductCategory(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProductsOrderedByProductCategory(int p0, com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getProductsOrderedByProductCategory(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public Collection getProducts(int supplierId, int productCategoryId ,IWTimestamp from, IWTimestamp to, String orderBy, int localeId, int filter) throws FinderException, RemoteException;
 public int getProductFilterNotConnectedToAnyProductCategory() throws RemoteException;
}