package is.idega.idegaweb.travel.service.hotel.business;

import java.rmi.RemoteException;

import is.idega.idegaweb.travel.business.BookerBean;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;

import com.idega.data.IDOLookup;
import com.idega.util.IWTimestamp;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelBookerBean extends BookerBean implements HotelBooker{

  public HotelBookerBean() {
  }
  
  public int getNumberOfReservedRooms(int hotelId, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException {
  		return ((GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class)).getNumberOfBookings(hotelId, fromStamp, toStamp, -1 );
  }
  
  public int getNumberOfReservedRooms(int[] resellerIds, int hotelId, IWTimestamp stamp) throws RemoteException {
  		return ((GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class)).getNumberOfBookings(resellerIds, hotelId, stamp, null);
  }
  
}
