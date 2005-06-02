package is.idega.idegaweb.travel.service.business;

import is.idega.idegaweb.travel.service.presentation.BookingForm;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.presentation.IWContext;


public interface ServiceHandler extends com.idega.business.IBOService
{
 public is.idega.idegaweb.travel.service.presentation.BookingForm getBookingForm(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1)throws java.lang.Exception, java.rmi.RemoteException;
 public BookingForm getBookingForm(IWContext iwc, Product product, boolean initializeBookingForm) throws Exception;
 public is.idega.idegaweb.travel.service.presentation.BookingOverview getBookingOverview(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getDepartureTime(com.idega.block.trade.stockroom.data.Product p0)throws java.sql.SQLException,java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.util.IWTimestamp getDepartureTime(int p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.presentation.DesignerForm getDesignerForm(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.ProductCategory p1)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.business.ProductBusiness getProductBusiness()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelStockroomBusiness getServiceBusiness(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getServiceLinks(com.idega.idegaweb.IWResourceBundle p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.presentation.ServiceOverview getServiceOverview(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.presentation.Voucher getVoucher(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.lang.Exception, java.rmi.RemoteException;
 public void removeProductApplication(com.idega.presentation.IWContext p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
