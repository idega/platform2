package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface BookingEntry extends com.idega.data.IDOEntity
{
 public is.idega.idegaweb.travel.interfaces.Booking getBooking()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingId() throws java.rmi.RemoteException;
 public int getCount() throws java.rmi.RemoteException;
 public java.util.Collection getEntries(com.idega.block.trade.stockroom.data.ProductPrice p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getEntries(is.idega.idegaweb.travel.interfaces.Booking p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public com.idega.block.trade.stockroom.data.ProductPrice getProductPrice()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public int getProductPriceId() throws java.rmi.RemoteException;
 public void setBookingId(int p0) throws java.rmi.RemoteException;
 public void setCount(int p0) throws java.rmi.RemoteException;
 public void setProductPriceId(int p0) throws java.rmi.RemoteException;
}
