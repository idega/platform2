package is.idega.idegaweb.travel.service.tour.business;

import java.rmi.RemoteException;

import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.interfaces.Booking;

import javax.ejb.*;

import com.idega.data.IDOException;
import com.idega.util.IWTimestamp;

public interface TourBooker extends com.idega.business.IBOService, Booker
{
 public int book(int bookingId, int pickupPlaceId, String roomNumber) throws IDOException, RemoteException;
 public Booking[] getBookings(int serviceId, IWTimestamp stamp, boolean withHotelPickup) throws RemoteException, FinderException;
}
