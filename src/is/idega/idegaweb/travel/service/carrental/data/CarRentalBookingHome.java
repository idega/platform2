package is.idega.idegaweb.travel.service.carrental.data;


public interface CarRentalBookingHome extends com.idega.data.IDOHome
{
 public CarRentalBooking create() throws javax.ejb.CreateException;
 public CarRentalBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}