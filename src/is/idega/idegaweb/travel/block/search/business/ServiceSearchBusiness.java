package is.idega.idegaweb.travel.block.search.business;

import com.idega.util.IWTimestamp;


public interface ServiceSearchBusiness extends com.idega.business.IBOService
{
 public java.util.HashMap checkResults(com.idega.presentation.IWContext p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelStockroomBusiness getBusiness(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getErrorFormFields(com.idega.presentation.IWContext p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.presentation.ui.DropdownMenu getPostalCodeDropdown(com.idega.idegaweb.IWResourceBundle p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.Object[] getPostalCodeIds(com.idega.presentation.IWContext p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.business.ServiceHandler getServiceHandler()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelSessionManager getTravelSessionManager(com.idega.idegaweb.IWUserContext p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection sortProducts(java.util.Collection p0,com.idega.block.trade.stockroom.data.PriceCategory p1, IWTimestamp bookingDate) throws java.rmi.RemoteException;
}
