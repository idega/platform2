package is.idega.idegaweb.travel.service.hotel.business;

import is.idega.idegaweb.travel.business.Booker;

public interface HotelBooker extends com.idega.business.IBOService, Booker
{
 public int getNumberOfReservedRooms(int[] p0,int p1,com.idega.util.IWTimestamp p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getNumberOfReservedRooms(int p0,com.idega.util.IWTimestamp p1,com.idega.util.IWTimestamp p2)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
