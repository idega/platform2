package is.idega.idegaweb.travel.data;


public interface BookingEntryHome extends com.idega.data.IDOHome
{
 public BookingEntry create() throws javax.ejb.CreateException;
 public BookingEntry createLegacy();
 public BookingEntry findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public BookingEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BookingEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}