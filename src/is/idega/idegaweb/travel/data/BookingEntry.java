package is.idega.idegaweb.travel.data;


public interface BookingEntry extends com.idega.data.IDOEntity
{
 public void setCount(int p0);
 public int getCount();
 public java.util.Collection getEntries(com.idega.block.trade.stockroom.data.ProductPrice p0)throws javax.ejb.FinderException;
 public void setProductPriceId(int p0);
 public void setBookingId(int p0);
 public com.idega.block.trade.stockroom.data.ProductPrice getProductPrice()throws javax.ejb.FinderException;
 public void initializeAttributes();
 public is.idega.idegaweb.travel.interfaces.Booking getBooking()throws java.rmi.RemoteException,javax.ejb.FinderException;
 public int getProductPriceId();
 public int getBookingId();
}
