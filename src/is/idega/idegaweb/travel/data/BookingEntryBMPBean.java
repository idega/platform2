package is.idega.idegaweb.travel.data;

import is.idega.idegaweb.travel.interfaces.Booking;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceHome;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class BookingEntryBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.BookingEntry {

  public BookingEntryBMPBean() {
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getBookingIDColumnName(), "booking id", true, true, Integer.class, "many-to-one", GeneralBooking.class);
    addAttribute(getCountColumnName(), "Fjöldi", true, true, Integer.class);
    addAttribute(getProductPriceIDColumnName(), "product price id", true, true, Integer.class, "many-to-one",ProductPrice.class);
  }
  public String getEntityName() {
    return getBookingEntriesTableName();
  }

  public ProductPrice getProductPrice() throws FinderException{
    ProductPriceHome ppHome = (com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class);
    return (ppHome).findByPrimaryKey(getProductPriceId());
  }

  public int getProductPriceId() {
    return getIntColumnValue(getProductPriceIDColumnName());
  }

  public void setProductPriceId(int id) {
    setColumn(getProductPriceIDColumnName(), id);
  }

  public void setBookingId(int id) {
    setColumn(getBookingIDColumnName(), id);
  }

  public int getBookingId() {
    return getIntColumnValue(getBookingIDColumnName());
  }

  public Booking getBooking() throws RemoteException, FinderException {
    GeneralBookingHome bHome = (is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class);
    return bHome.findByPrimaryKey(new Integer(getBookingId()));
  }

  public void setCount(int count) {
    setColumn(getCountColumnName(), count);
  }

  public int getCount() {
    return getIntColumnValue(getCountColumnName());
  }

  public static String getBookingEntriesTableName() {return "TB_BOOKING_ENTRIES";}
  public static String getBookingIDColumnName() {return "BOOKING_ID";}
  public static String getCountColumnName() {return "PASSENGER_COUNT";}
  public static String getProductPriceIDColumnName() {return "SR_PRODUCT_PRICE_ID";}

  public Collection ejbHomeGetEntries(Booking booking) throws FinderException, RemoteException{
    return super.idoFindAllIDsByColumnBySQL(getBookingIDColumnName(), Integer.toString(booking.getID()));
//    return null;
  }

  public Collection getEntries(ProductPrice pPrice) throws FinderException {
    return super.idoFindAllIDsByColumnBySQL(getProductPriceIDColumnName(), Integer.toString(pPrice.getID()));
  }

}
