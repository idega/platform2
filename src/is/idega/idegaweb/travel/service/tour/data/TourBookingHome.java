package is.idega.idegaweb.travel.service.tour.data;


public interface TourBookingHome extends com.idega.data.IDOHome
{
 public TourBooking create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public TourBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}