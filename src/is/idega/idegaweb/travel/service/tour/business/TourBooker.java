package is.idega.idegaweb.travel.service.tour.business;

import is.idega.idegaweb.travel.business.Booker;

import com.idega.util.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.tour.data.*;

import java.sql.SQLException;
import java.util.*;

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


  public static int BookBySupplier(int serviceId, int hotelPickupPlaceId, String roomNumber, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId) throws SQLException {
    int _bookingId = Booker.Book(serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, postalCode, paymentType, userId, ownerId, addressId);
    return Book(_bookingId, hotelPickupPlaceId, roomNumber);
  }

  public static int Book(int serviceId, int hotelPickupPlaceId, String roomNumber, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, int bookingType, String postalCode, int paymentType, int userId, int ownerId, int addressId) throws SQLException {
    int _bookingId = Booker.Book(serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, bookingType, postalCode, paymentType, userId, ownerId, addressId);
    return Book(_bookingId, hotelPickupPlaceId, roomNumber);
  }

  public static int updateBooking(int bookingId, int serviceId, int hotelPickupPlaceId, String roomNumber, String country, String name, String address, String city, String telephoneNumber, String email, idegaTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId) throws SQLException {
    int _bookingId = Booker.updateBooking(bookingId, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount,  postalCode, paymentType, userId, ownerId, addressId);
    return Book(_bookingId, hotelPickupPlaceId, roomNumber);
  }

  private static int Book(int bookingId, int hotelPickupPlaceId, String roomNumber) throws SQLException {
    try {
      boolean update = false;
      TourBooking booking = null;
      try {
        booking = ((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHomeLegacy(TourBooking.class)).findByPrimaryKeyLegacy(bookingId);
        update = true;
      }catch (Exception sql) {
        booking = ((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHomeLegacy(TourBooking.class)).createLegacy();
        booking.setColumn(booking.getIDColumnName(), bookingId);
      }
      if (booking == null) {
        System.err.println("TourBooker : booking  == null !!!");
        System.err.println("...bookingId          =  "+bookingId);
        System.err.println("...hotelPickupPlaceId =  "+hotelPickupPlaceId);
        System.err.println("...roomNumber         =  "+bookingId);
      }

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


      if (update) {
        booking.update();
      } else {
        booking.insert();
      }


      return bookingId;
    }catch (SQLException s) {




      s.printStackTrace(System.err);
      return bookingId;
    }


  }

  public static Booking[] getBookings(int serviceId, idegaTimestamp stamp, boolean withHotelPickup) {
    Booking[] bookings = getBookings(serviceId, stamp);
    try {
      List bings = new Vector();
      TourBooking tb;
      for (int i = 0; i < bookings.length; i++) {
        try {
          tb = ((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHomeLegacy(TourBooking.class)).findByPrimaryKeyLegacy(bookings[i].getID());
          if (tb.getHotelPickupPlaceID() != -1) {
            bings.add(bookings[i]);
          }
        }catch (SQLException sql) {
          System.err.println("TourBooker : getBookings : "+sql.getMessage());
        }
      }
      if (bookings.length > 0) {
        bookings = (Booking[]) bings.toArray(new Booking[]{});
      }
    }catch (javax.ejb.EJBException ejb) {
      ejb.printStackTrace(System.err);
    }
    return bookings;
  }

}
