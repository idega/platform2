package is.idega.idegaweb.travel.data;


public interface HotelPickupPlaceHome extends com.idega.data.IDOHome
{
 public HotelPickupPlace create() throws javax.ejb.CreateException;
 public HotelPickupPlace createLegacy();
 public HotelPickupPlace findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public HotelPickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public HotelPickupPlace findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}