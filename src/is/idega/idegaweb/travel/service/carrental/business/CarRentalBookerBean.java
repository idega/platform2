package is.idega.idegaweb.travel.service.carrental.business;

import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.business.BookerBean;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBooking;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalBookingHome;

import java.rmi.RemoteException;

import javax.ejb.CreateException;

import com.idega.business.IBOLookup;
import com.idega.data.IDOException;
import com.idega.util.IWTimestamp;

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

  public int book(int bookingId, int pickupPlaceId, IWTimestamp pickupTime, int dropoffPlaceId, IWTimestamp dropoffTime) throws IDOException, RemoteException {
		try {

		  CarRentalBooking booking = null;
		  try {
				booking = ((CarRentalBookingHome)com.idega.data.IDOLookup.getHome(CarRentalBooking.class)).findByPrimaryKey(new Integer(bookingId));
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
	
			booking.store();
	
		  return Integer.parseInt(booking.getPrimaryKey().toString());
		}catch (CreateException s) {
		  s.printStackTrace(System.err);
		  return bookingId;
		}
	
	
  }
	
}
