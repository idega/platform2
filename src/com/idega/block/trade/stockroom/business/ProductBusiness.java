package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.util.List;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.data.IDOFinderException;
import com.idega.util.IWTimestamp;


public interface ProductBusiness extends com.idega.business.IBOService
{
 public void clearProductCache(int p0) throws java.rmi.RemoteException;
 public int createProduct(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,int[] p6,int p7)throws java.lang.Exception, java.rmi.RemoteException;
 public int createProduct(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,boolean p6,int[] p7,int p8)throws java.lang.Exception, java.rmi.RemoteException;
 public int createProduct(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,boolean p6,int[] p7,int p8,int p9)throws java.lang.Exception, java.rmi.RemoteException;
 public int createProduct(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,boolean p4)throws java.lang.Exception, java.rmi.RemoteException;
 public int createProduct(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,boolean p4,int p5)throws java.lang.Exception, java.rmi.RemoteException;
 public void deleteProduct(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,com.idega.data.IDOException, java.rmi.RemoteException;
 public com.idega.core.location.data.Address getArrivalAddress(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,com.idega.data.IDOFinderException,java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.core.location.data.Address[] getArrivalAddresses(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.TravelAddress getDepartureAddress(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,com.idega.data.IDOFinderException,java.sql.SQLException, java.rmi.RemoteException;
 public java.util.List getDepartureAddresses(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1,boolean p2)throws java.rmi.RemoteException,com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public java.util.List getDepartureAddresses(com.idega.block.trade.stockroom.data.Product p0,boolean p1)throws java.rmi.RemoteException,com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered, String key) throws RemoteException, IDOFinderException;
 public com.idega.presentation.ui.DropdownMenu getDropdownMenuWithProducts(com.idega.presentation.IWContext p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getDropdownMenuWithProducts(com.idega.presentation.IWContext p0,int p1,java.lang.String p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getDropdownMenuWithProducts(com.idega.presentation.IWContext p0,java.util.List p1,java.lang.String p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getLocaleDropDown(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public java.lang.String getParameterLocaleDrop() throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product getProduct(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product getProduct(java.lang.Integer p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProductCategories()throws com.idega.data.IDOFinderException, java.rmi.RemoteException;
 public java.util.List getProductCategories(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,com.idega.data.IDORelationshipException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductCategory getProductCategory(int p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductHome getProductHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getProductIdParameter() throws java.rmi.RemoteException;
 public java.lang.String getProductNameWithNumber(com.idega.block.trade.stockroom.data.Product p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getProductNameWithNumber(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getProductNameWithNumber(com.idega.block.trade.stockroom.data.Product p0,boolean p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getProductNameWithNumber(com.idega.block.trade.stockroom.data.Product p0,boolean p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getProducts(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts(com.idega.block.trade.stockroom.data.ProductCategory p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts(com.idega.block.category.data.ICCategory p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts(com.idega.presentation.IWContext p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getProducts(com.idega.util.IWTimestamp p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.List getProducts(int p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts(int p0,int p1,com.idega.util.IWTimestamp p2,com.idega.util.IWTimestamp p3)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getProducts(java.util.List p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public int getSelectedLocaleId(com.idega.presentation.IWContext p0) throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Timeframe getTimeframe(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Timeframe getTimeframe(com.idega.block.trade.stockroom.data.Product p0,com.idega.util.IWTimestamp p1,int p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public Timeframe getTimeframe(Product product, Timeframe[] timeframes, IWTimestamp stamp, int travelAddressId) throws RemoteException;
 public int updateProduct(int p0,int p1,java.lang.Integer p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,boolean p6,int[] p7,int p8)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Product updateProduct(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,javax.ejb.FinderException,com.idega.data.IDOException, java.rmi.RemoteException;
 public int updateProduct(int p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,boolean p5,int p6)throws java.lang.Exception, java.rmi.RemoteException;
 public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered, String key, Timeframe[] timeframes) throws RemoteException, IDOFinderException;
}
