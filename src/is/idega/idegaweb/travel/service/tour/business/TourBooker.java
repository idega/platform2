package is.idega.idegaweb.travel.service.tour.business;

import is.idega.idegaweb.travel.business.Booker;

import com.idega.util.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.tour.data.*;

import java.sql.SQLException;

/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TourBooker extends Booker {

  public TourBooker() {
  }


  public static int BookBySupplier(int serviceId, int hotelPickupPlaceId, String roomNumber, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType) throws SQLException {
    int _bookingId = Booker.Book(serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, postalCode, paymentType);
    return Book(_bookingId, hotelPickupPlaceId, roomNumber);
  }

  public static int Book(int serviceId, int hotelPickupPlaceId, String roomNumber, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, int bookingType, String postalCode, int paymentType) throws SQLException {
    int _bookingId = Booker.Book(serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, bookingType, postalCode, paymentType);
    return Book(_bookingId, hotelPickupPlaceId, roomNumber);
  }

  public static int updateBooking(int bookingId, int serviceId, int hotelPickupPlaceId, String roomNumber, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType) throws SQLException {
    int _bookingId = Booker.updateBooking(bookingId, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount,  postalCode, paymentType);
    return Book(_bookingId, hotelPickupPlaceId, roomNumber);
  }

  private static int Book(int bookingId, int hotelPickupPlaceId, String roomNumber) throws SQLException {
    try {
      TourBooking booking = new TourBooking(bookingId);
      if (hotelPickupPlaceId != -1) {
        booking.setHotelPickupPlaceID(hotelPickupPlaceId);
        if (roomNumber != null) {
          booking.setRoomNumber(roomNumber);
        }
      }

      if (hotelPickupPlaceId != -1) {
        booking.setHotelPickupPlaceID(hotelPickupPlaceId);
        if (roomNumber != null) {
          booking.setRoomNumber(roomNumber);
        }
      }
      booking.update();
      return bookingId;
    }catch (SQLException s) {
      s.printStackTrace(System.err);
      return bookingId;
    }


  }

}
