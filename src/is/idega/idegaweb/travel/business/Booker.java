package is.idega.travel.business;

import java.sql.SQLException;
import com.idega.data.EntityControl;
import com.idega.data.SimpleQuerier;
import com.idega.util.idegaTimestamp;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.ProductPrice;
import is.idega.travel.data.BookingEntry;
import is.idega.travel.data.Booking;


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

  public static int BookBySupplier(int serviceId, int hotelPickupPlaceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode) throws SQLException {
    return Book(serviceId, hotelPickupPlaceId, country, name, address, city, telephoneNumber, email, date, totalCount, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, postalCode);
  }

  public static int Book(int serviceId, int hotelPickupPlaceId, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, int bookingType, String postalCode) throws SQLException {
    Booking booking = new Booking();
      booking.setServiceID(serviceId);
      booking.setAddress(address);
      booking.setBookingDate(date.getTimestamp());
      booking.setBookingTypeID(bookingType);
      booking.setCity(city);
      booking.setCountry(country);
      booking.setDateOfBooking(idegaTimestamp.getTimestampRightNow());
      booking.setEmail(email);
      if (hotelPickupPlaceId != -1) {
        booking.setHotelPickupPlaceID(hotelPickupPlaceId);
      }
      booking.setName(name);
      booking.setPostalCode(postalCode);
      booking.setTelephoneNumber(telephoneNumber);
//      booking.setProductPriceId(productPriceId);
      booking.setTotalCount(totalCount);
    booking.insert();

    return booking.getID();
  }


  public static int getNumberOfBookings(int resellerId, int serviceId, idegaTimestamp stamp) {
    int returner = 0;
    try {
        Booking booking = (Booking) (Booking.getStaticInstance(Booking.class));
        Reseller reseller = (Reseller) (Reseller.getStaticInstance(Reseller.class));

        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select sum(b."+Booking.getTotalCountColumnName()+") from "+Booking.getBookingTableName()+" b, "+EntityControl.getManyToManyRelationShipTableName(Booking.class,Reseller.class)+" br");
            sql.append(" where ");
            sql.append(" br."+reseller.getIDColumnName()+" = "+resellerId);
            sql.append(" and ");
            sql.append(" b."+booking.getIDColumnName()+" = br."+booking.getIDColumnName());
            sql.append(" and ");
            sql.append(" b."+Booking.getIsValidColumnName()+"='Y'");
            sql.append(" and ");
            sql.append(" b."+Booking.getServiceIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append(" b."+Booking.getBookingDateColumnName()+" = '"+stamp.toSQLDateString()+"'");
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
    try {
        String[] many = {};
          StringBuffer sql = new StringBuffer();
            sql.append("Select "+Booking.getTotalCountColumnName()+" from "+Booking.getBookingTableName());
            sql.append(" where ");
            sql.append(Booking.getServiceIDColumnName()+"="+serviceId);
            sql.append(" and ");
            sql.append(Booking.getIsValidColumnName()+" = 'Y'");
            if ( (fromStamp != null) && (toStamp == null) ) {
              sql.append(" and ");
              sql.append(Booking.getBookingDateColumnName()+" = '"+fromStamp.toSQLDateString()+"'");
            }else if ( (fromStamp != null) && (toStamp != null)) {
              sql.append(" and (");
              sql.append(Booking.getBookingDateColumnName()+" >= '"+fromStamp.toSQLDateString()+"'");
              sql.append(" and ");
              sql.append(Booking.getBookingDateColumnName()+" <= '"+toStamp.toSQLDateString()+"')");
            }
            if (bookingType != -1) {
              sql.append(" and ");
              sql.append(Booking.getBookingTypeIDColumnName()+" = "+bookingType);
            }
        many = SimpleQuerier.executeStringQuery(sql.toString());

        for (int i = 0; i < many.length; i++) {
            returner += Integer.parseInt(many[i]);
        }


    }catch (Exception e) {
        e.printStackTrace(System.err);
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
        sql.append("Select * from "+Booking.getBookingTableName());
        sql.append(" where ");
        sql.append(Booking.getServiceIDColumnName()+"="+serviceId);
        sql.append(" and ");
        sql.append(Booking.getIsValidColumnName()+" = 'Y'");
        sql.append(" and ");
        sql.append(Booking.getBookingDateColumnName()+" = '"+stamp.toSQLDateString()+"'");
        if (bookingTypeIds != null) {
          if (bookingTypeIds.length > 0 ) {
            sql.append(" and (");
            for (int i = 0; i < bookingTypeIds.length; i++) {
              if (bookingTypeIds[i] != -1) {
                if (i > 0) sql.append(" OR ");
                sql.append(Booking.getBookingTypeIDColumnName()+" = "+bookingTypeIds[i]);
              }
            }
            sql.append(") ");
          }
        }

        returner = (Booking[]) (new Booking()).findAll(sql.toString());
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public static float getBookingPrice(Booking booking) {
      float total = 0;

      try {

        BookingEntry[] entries = booking.getBookingEntries();

        float price;
        ProductPrice pPrice;

        for (int i = 0; i < entries.length; i++) {
          pPrice = entries[i].getProductPrice();
          price = TravelStockroomBusiness.getPrice(booking.getServiceID(), pPrice.getPriceCategoryID(), pPrice.getCurrencyId(), booking.getDateOfBooking());
          total += price * entries[i].getCount();
        }

      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }


      return total;
  }

}