package is.idega.idegaweb.travel.business;

import javax.ejb.*;

public interface TravelSessionManager extends com.idega.business.IBOSession
{
 public com.idega.idegaweb.IWBundle getIWBundle() throws java.rmi.RemoteException;
 public int getUserId() throws java.rmi.RemoteException;
 public int getLocaleId() throws java.rmi.RemoteException;
 public void setReseller(com.idega.block.trade.stockroom.data.Reseller p0) throws java.rmi.RemoteException;
 public java.util.Locale getLocale() throws java.rmi.RemoteException;
 public com.idega.core.user.data.User getUser() throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Reseller getReseller() throws java.rmi.RemoteException;
 public void setSupplier(com.idega.block.trade.stockroom.data.Supplier p0) throws java.rmi.RemoteException;
 public com.idega.idegaweb.IWResourceBundle getIWResourceBundle() throws java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.Supplier getSupplier() throws java.rmi.RemoteException;
 public void clearLocale() throws java.rmi.RemoteException;
}
