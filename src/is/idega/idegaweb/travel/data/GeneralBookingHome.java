package is.idega.idegaweb.travel.data;


public interface GeneralBookingHome extends com.idega.data.IDOHome
{
 public GeneralBooking create() throws javax.ejb.CreateException;
 public GeneralBooking createLegacy();
 public GeneralBooking findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public GeneralBooking findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public GeneralBooking findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}