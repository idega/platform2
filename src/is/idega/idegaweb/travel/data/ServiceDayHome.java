package is.idega.idegaweb.travel.data;


public interface ServiceDayHome extends com.idega.data.IDOHome
{
 public ServiceDay create() throws javax.ejb.CreateException;
 public ServiceDay createLegacy();
 public ServiceDay findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ServiceDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ServiceDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}