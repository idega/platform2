package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface BookingEntry extends com.idega.data.IDOLegacyEntity
{
 public is.idega.idegaweb.travel.interfaces.Booking getBooking()throws java.sql.SQLException;
 public int getBookingId();
 public int getCount();
 public com.idega.block.trade.stockroom.data.ProductPrice getProductPrice()throws java.sql.SQLException;
 public int getProductPriceId();
 public void setBookingId(int p0);
 public void setCount(int p0);
 public void setProductPriceId(int p0);
}
