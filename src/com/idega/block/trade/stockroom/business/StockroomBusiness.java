package com.idega.block.trade.stockroom.business;

import javax.ejb.*;

public interface StockroomBusiness extends com.idega.business.IBOService
{
 public void addSupplies(int p0,float p1) throws java.rmi.RemoteException;
 public int createPriceCategory(int p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws java.sql.SQLException, java.rmi.RemoteException;
 public void createPriceDiscountCategory(int p0,int p1,java.lang.String p2,java.lang.String p3,java.lang.String p4)throws java.sql.SQLException, java.rmi.RemoteException;
 public int createProduct(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,int[] p6,int p7)throws java.lang.Exception, java.rmi.RemoteException;
 public void depleteSupplies(int p0,float p1) throws java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getCurrencyDropdownMenu(java.lang.String p0) throws java.rmi.RemoteException;
 public float getDiscount(int p0,int p1,java.sql.Timestamp p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductPrice getPrice(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public float getPrice(int p0,int p1,int p2,int p3,java.sql.Timestamp p4)throws java.sql.SQLException, java.rmi.RemoteException;
 public float getPrice(int p0,int p1,int p2,int p3,java.sql.Timestamp p4,int p5,int p6)throws java.sql.SQLException, java.rmi.RemoteException;
 public float getSupplyStatus(int p0,java.sql.Timestamp p1) throws java.rmi.RemoteException;
 public float getSupplyStatus(int p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public int getUserResellerId(com.idega.presentation.IWContext p0)throws java.sql.SQLException,java.lang.RuntimeException, java.rmi.RemoteException;
 public int getUserResellerId(com.idega.core.user.data.User p0)throws java.sql.SQLException,java.lang.RuntimeException, java.rmi.RemoteException;
 public int getUserSupplierId(com.idega.presentation.IWContext p0)throws java.sql.SQLException,java.lang.RuntimeException, java.rmi.RemoteException;
 public int getUserSupplierId(com.idega.core.user.data.User p0)throws java.sql.SQLException,java.lang.RuntimeException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductPrice setPrice(int p0,int p1,int p2,int p3,java.sql.Timestamp p4,float p5,int p6,int p7,int p8)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductPrice setPrice(int p0,int p1,int p2,int p3,java.sql.Timestamp p4,float p5,int p6,int p7,int p8,int p9)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductPrice setPrice(int p0,int p1,int p2,java.sql.Timestamp p3,float p4,int p5,int p6,int p7)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductPrice setPrice(int p0,int p1,int p2,java.sql.Timestamp p3,float p4,int p5,int p6,int p7,int p8)throws java.sql.SQLException, java.rmi.RemoteException;
 public void setSupplyStatus(int p0,float p1) throws java.rmi.RemoteException;
 public int updateProduct(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,boolean p6,int[] p7,int p8)throws java.lang.Exception, java.rmi.RemoteException;
 public boolean isInTimeframe(com.idega.util.IWTimestamp p0, com.idega.util.IWTimestamp p1, com.idega.util.IWTimestamp p2, boolean p3);
 public boolean isBetween(com.idega.util.IWTimestamp p0, com.idega.util.IWTimestamp p1, com.idega.util.IWTimestamp p2, boolean p3, boolean p4);
 
}
