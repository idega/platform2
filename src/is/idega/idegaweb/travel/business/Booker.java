package is.idega.idegaweb.travel.business;

import com.idega.presentation.ui.*;
import java.sql.SQLException;
import com.idega.data.EntityControl;
import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.ProductPrice;
import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.GeneralBooking;
import com.idega.util.database.ConnectionBroker;
import java.sql.Connection;
import is.idega.idegaweb.travel.interfaces.Booking;
import com.idega.block.trade.data.Currency;
import com.idega.idegaweb.*;

import is.idega.idegaweb.travel.service.tour.data.*;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Booker {

  public Booker() {
  }

  public static int BookBySupplier(int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType) throws SQLException {
    return Book(-1, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, postalCode, paymentType);
  }

  public static int Book(int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, int bookingType, String postalCode, int paymentType) throws SQLException {
    return Book(-1, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, bookingType, postalCode, paymentType);
  }

  public static int updateBooking(int bookingId, int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType) throws SQLException {
    return Book(bookingId, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, -1, postalCode, paymentType);
  }

  private static int Book(int bookingId, int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, int bookingType, String postalCode, int paymentTypeId) throws SQLException {
    Booking booking = null;
    int returner = bookingId;
    Object type = getServiceType(serviceId);

    if (type != null)
    if (bookingId == -1) {
      if (type instanceof Tour) booking = new TourBooking();
        booking.setServiceID(serviceId);
        booking.setAddress(address);
        booking.setBookingDate(date.getTimestamp());
        booking.setBookingTypeID(bookingType);
        booking.setCity(city);
        booking.setCountry(country);
        booking.setDateOfBooking(idegaTimestamp.getTimestampRightNow());
        booking.setEmail(email);
        booking.setName(name);
        booking.setPostalCode(postalCode);
        booking.setPaymentTypeId(paymentTypeId);
        booking.setTelephoneNumber(telephoneNumber);
        booking.setTotalCount(totalCount);
      booking.insert();

      returner =  booking.getID();
    }else {
      if (type instanceof Tour) booking = new TourBooking(bookingId);
        booking.setServiceID(serviceId);
        booking.setAddress(address);
        booking.setBookingDate(date.getTimestamp());
        if (bookingType != -1)
        booking.setBookingTypeID(bookingType);
        booking.setCity(city);
        booking.setCountry(country);
        booking.setDateOfBooking(idegaTimestamp.getTimestampRightNow());
        booking.setEmail(email);
        booking.setName(name);
        booking.setPostalCode(postalCode);
        booking.setPaymentTypeId(paymentTypeId);
        booking.setTelephoneNumber(telephoneNumber);
        booking.setTotalCount(totalCount);
      booking.update();

    }

    return returner;
  }


  public static int getNumberOfBookings(int resellerId, int serviceId, idegaTimestamp stamp) {
    int returner = 0;
    try {
        GeneralBooking booking = (GeneralBooking) (GeneralBooking.getStaticInstance(GeneralBooking.class));
        Reseller reseller = (Reseller) (Reseller.getStaticInstance(Reseller.class));

        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select sum(b."+GeneralBooking.getTotalCountColumnName()+") from "+GeneralBooking.getBookingTableName()+" b, "+EntityControl.getManyToManyRelationShipTableName(GeneralBooking.class,Reseller.class)+" br");
            sql.append(" where ");
            sql.append(" br."+reseller.getIDColumnName()+" = "+resellerId);
            sql.append(" and ");
            sql.append(" b."+booking.getIDColumnName()+" = br."+booking.getIDColumnName());
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getIsValidColumnName()+"='Y'");
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getServiceIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append(" b."+GeneralBooking.getBookingDateColumnName()+" = '"+stamp.toSQLDateString()+"'");
        many = SimpleQuerier.executeStringQuery(sql.toString());

        if (many != null) {
          if (many[0] != null)
            returner = Integer.parseInt(many[0]);
        }


    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp stamp){
      return getNumberOfBookings(serviceId, stamp, null);
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp stamp, int bookingType){
      return getNumberOfBookings(serviceId, stamp, null, bookingType);
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp fromStamp, idegaTimestamp toStamp){
      return getNumberOfBookings(serviceId, fromStamp, toStamp, -1);
  }

  public static int getNumberOfBookings(int serviceId, idegaTimestamp fromStamp, idegaTimestamp toStamp, int bookingType){
    int returner = 0;
    Connection conn = null;
    try {
      conn = ConnectionBroker.getConnection();
        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select "+GeneralBooking.getTotalCountColumnName()+" from "+GeneralBooking.getBookingTableName());
            sql.append(" where ");
            sql.append(GeneralBooking.getServiceIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append(GeneralBooking.getIsValidColumnName()+" = 'Y'");
            if ( (fromStamp != null) && (toStamp == null) ) {
              sql.append(" and ");
              sql.append(GeneralBooking.getBookingDateColumnName()+" containing '"+fromStamp.toSQLDateString()+"'");
            }else if ( (fromStamp != null) && (toStamp != null)) {
              sql.append(" and (");
              sql.append(GeneralBooking.getBookingDateColumnName()+" >= '"+fromStamp.toSQLDateString()+"'");
              sql.append(" and ");
              sql.append(GeneralBooking.getBookingDateColumnName()+" <= '"+toStamp.toSQLDateString()+"')");
            }
            if (bookingType != -1) {
              sql.append(" and ");
              sql.append(GeneralBooking.getBookingTypeIDColumnName()+" = "+bookingType);
            }

        many = SimpleQuerier.executeStringQuery(sql.toString(),conn);


        for (int i = 0; i < many.length; i++) {
            returner += Integer.parseInt(many[i]);
        }

    }catch (Exception e) {
        e.printStackTrace(System.err);
    }finally {
      ConnectionBroker.freeConnection(conn);
    }

    return returner;
  }

  public static Booking[] getBookings(int serviceId, idegaTimestamp stamp) {
    return getBookings(serviceId,stamp,new int[]{});
  }

  public static Booking[] getBookings(int serviceId, idegaTimestamp stamp, int bookingTypeId) {
    return getBookings(serviceId,stamp,new int[]{bookingTypeId});
  }

  public static Booking[] getBookings(int serviceId, idegaTimestamp stamp, int[] bookingTypeIds) {
    Booking[] returner = {};
    StringBuffer sql = new StringBuffer();
    try {

        sql.append("Select * from "+GeneralBooking.getBookingTableName());
        sql.append(" where ");
        sql.append(GeneralBooking.getServiceIDColumnName()+"="+serviceId);
        sql.append(" and ");
        sql.append(GeneralBooking.getIsValidColumnName()+" = 'Y'");
        sql.append(" and ");
        sql.append(GeneralBooking.getBookingDateColumnName()+" containing '"+stamp.toSQLDateString()+"'");
        if (bookingTypeIds != null) {
          if (bookingTypeIds.length > 0 ) {
            sql.append(" and (");
            for (int i = 0; i < bookingTypeIds.length; i++) {
              if (bookingTypeIds[i] != -1) {
                if (i > 0) sql.append(" OR ");
                sql.append(GeneralBooking.getBookingTypeIDColumnName()+" = "+bookingTypeIds[i]);
              }
            }
            sql.append(") ");
          }
        }

        returner = (GeneralBooking[]) (new GeneralBooking()).findAll(sql.toString());
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public static float getBookingEntryPrice(BookingEntry entry, Booking booking) {
      float total = 0;

      try {
        ProductPrice pPrice;

        pPrice = entry.getProductPrice();
        total = entry.getCount() * TravelStockroomBusiness.getPrice(booking.getServiceID(), pPrice.getPriceCategoryID(), pPrice.getCurrencyId(), idegaTimestamp.getTimestampRightNow());

      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }catch (com.idega.block.trade.stockroom.business.ProductPriceException ppe) {
        ppe.printStackTrace(System.err);
      }


      return total;
  }

  public static float getBookingPrice(Booking booking) {
      float total = 0;

      try {

        BookingEntry[] entries = booking.getBookingEntries();

        float price;
        ProductPrice pPrice;

        for (int i = 0; i < entries.length; i++) {
          total += getBookingEntryPrice(entries[i], booking);
        }

      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }


      return total;
  }

  public static BookingEntry[] getBookingEntries(Booking booking) {
    try {
      return booking.getBookingEntries();
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return null;
    }
  }

  public static boolean deleteBooking(int bookingId) {
    try {
      Booking booking = new GeneralBooking(bookingId);
      return deleteBooking(booking);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return false;
    }
  }

  public static boolean deleteBooking(Booking booking) {
    try {
      booking.setIsValid(false);
      booking.update();
      return true;
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return false;
    }
  }

  /**
   * @todo finna betri stað
   */
  private static Object getServiceType(int serviceId) {
    Object object;
    try {
      object = new Tour(serviceId);
      return object;
    }catch (Exception e) {
      return null;
    }
  }

  public static DropdownMenu getPaymentTypes(IWResourceBundle iwrb) {
    DropdownMenu menu = new DropdownMenu("payment_type");
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_NO_PAYMENT, iwrb.getLocalizedString("travel.unpaid","Unpaid"));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_CASH, iwrb.getLocalizedString("travel.cash","Cash"));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_CREDIT_CARD ,iwrb.getLocalizedString("travel.credit_card","Credit card"));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_VOUCHER ,iwrb.getLocalizedString("travel.voucher","Voucher"));
      menu.setAttribute("style","font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    return menu;
  }

  public static Currency getCurrency(Booking booking) throws SQLException {
      Currency currency = null;
      BookingEntry[] entries = booking.getBookingEntries();

      if (entries.length > 0)
        currency = new Currency(entries[0].getProductPrice().getCurrencyId());

      return currency;
  }


}
