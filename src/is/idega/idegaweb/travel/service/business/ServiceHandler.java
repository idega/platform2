package is.idega.idegaweb.travel.service.business;

import javax.ejb.*;

public interface ServiceHandler extends com.idega.business.IBOService
{
 public is.idega.idegaweb.travel.service.presentation.BookingForm getBookingForm(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.Product p1)throws java.lang.Exception, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.presentation.DesignerForm getDesignerForm(com.idega.presentation.IWContext p0,com.idega.block.trade.stockroom.data.ProductCategory p1)throws java.lang.Exception, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.presentation.Voucher getVoucher(is.idega.idegaweb.travel.interfaces.Booking booking) throws Exception;
}
