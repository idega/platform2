package is.idega.idegaweb.travel.service.carrental.business;

import java.rmi.RemoteException;

import com.idega.data.IDOException;
import com.idega.util.IWTimestamp;

import is.idega.idegaweb.travel.business.Booker;


public interface CarRentalBooker extends com.idega.business.IBOService, Booker
{
	public int book(int bookingId, int pickupPlaceId, IWTimestamp pickupTime, int dropoffPlaceId, IWTimestamp dropoffTime) throws IDOException, RemoteException;
}
