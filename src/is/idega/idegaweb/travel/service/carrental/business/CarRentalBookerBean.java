package is.idega.idegaweb.travel.service.carrental.business;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.CreateException;

import com.idega.business.IBOLookup;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBooking;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBookingHome;

/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class CarRentalBookerBean extends BookerBean implements CarRentalBooker {

  public CarRentalBookerBean() {
  }

  private Booker getBooker() throws RemoteException {
		return (Booker) IBOLookup.getServiceInstance(getIWApplicationContext(), Booker.class);
  }	
  
  public int BookBySupplier(int serviceId, int pickupPlaceId, IWTimestamp pickupTime, int dropoffPlaceId, IWTimestamp dropoffTime, String country, String name, String address, String city, String telephoneNumber, String email, IWTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId, String comment) throws SQLException, RemoteException, CreateException {
		int _bookingId = getBooker().Book(serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, postalCode, paymentType, userId, ownerId, addressId, comment);
		return Book(_bookingId, pickupPlaceId, pickupTime, dropoffPlaceId, dropoffTime);
  }

  public int Book(int serviceId, int pickupPlaceId, IWTimestamp pickupTime, int dropoffPlaceId, IWTimestamp dropoffTime, String country, String name, String address, String city, String telephoneNumber, String email, IWTimestamp date, int totalCount, int bookingType, String postalCode, int paymentType, int userId, int ownerId, int addressId, String comment) throws SQLException, RemoteException, CreateException {
		int _bookingId = getBooker().Book(serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, bookingType, postalCode, paymentType, userId, ownerId, addressId, comment);
		return Book(_bookingId, pickupPlaceId, pickupTime, dropoffPlaceId, dropoffTime);
  }

  public int updateBooking(int bookingId, int serviceId, int pickupPlaceId, IWTimestamp pickupTime, int dropoffPlaceId, IWTimestamp dropoffTime, String country, String name, String address, String city, String telephoneNumber, String email, IWTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId, String comment) throws SQLException, RemoteException, CreateException {
		int _bookingId = getBooker().updateBooking(bookingId, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount,  postalCode, paymentType, userId, ownerId, addressId, comment);
		return Book(_bookingId, pickupPlaceId, pickupTime, dropoffPlaceId, dropoffTime);
  }

  private int Book(int bookingId, int pickupPlaceId, IWTimestamp pickupTime, int dropoffPlaceId, IWTimestamp dropoffTime) throws SQLException, RemoteException {
		try {
			System.out.println("[CarRentalBooking] Book()");
		  boolean update = false;
		  CarRentalBooking booking = null;
		  try {
				booking = ((CarRentalBookingHome)com.idega.data.IDOLookup.getHome(CarRentalBooking.class)).findByPrimaryKey(new Integer(bookingId));
				update = true;
		  }catch (Exception sql) {
				booking = ((CarRentalBookingHome)com.idega.data.IDOLookup.getHome(CarRentalBooking.class)).create();
				booking.setPrimaryKey(new Integer(bookingId));
		  }

			if (pickupPlaceId > 0) {
				booking.setPickupPlaceID(pickupPlaceId);
				booking.setPickupTime(pickupTime.getTimestamp());
			}
			if (dropoffPlaceId > 0) {
				booking.setDropoffPlaceId(dropoffPlaceId);
				booking.setDropoffTime(dropoffTime.getTimestamp());
			}
	
		  if (update) {
			booking.store();
		  } else {
			booking.store();
		  }
	
	
		  return bookingId;
		}catch (CreateException s) {
	
	
	
	
		  s.printStackTrace(System.err);
		  return bookingId;
		}
	
	
  }
	
}
