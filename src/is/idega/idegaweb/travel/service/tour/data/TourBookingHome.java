package is.idega.idegaweb.travel.service.tour.data;


public interface TourBookingHome extends com.idega.data.IDOHome
{
 public TourBooking create() throws javax.ejb.CreateException;
 public TourBooking createLegacy();
 public TourBooking findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TourBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TourBooking findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}