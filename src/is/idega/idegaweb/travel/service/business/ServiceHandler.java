package is.idega.idegaweb.travel.service.business;

import javax.ejb.*;

public interface ServiceHandler extends com.idega.business.IBOService
{
 public is.idega.idegaweb.travel.service.presentation.BookingForm getBookingForm(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.util.IWTimeStamp getDepartureTime(int p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public com.idega.util.IWTimeStamp getDepartureTime(com.idega.block.trade.stockroom.data.Product p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.presentation.DesignerForm getDesignerForm(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.ProductCategory p1)throws java.lang.Exception, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.presentation.Voucher getVoucher(is.idega.idegaweb.travel.interfaces.Booking p0)throws java.lang.Exception, java.rmi.RemoteException;
 public void removeProductApplication(com.idega.presentation.IWContext p0,int p1) throws java.rmi.RemoteException;
}
