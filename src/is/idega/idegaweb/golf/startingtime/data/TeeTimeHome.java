package is.idega.idegaweb.golf.startingtime.data;


public interface TeeTimeHome extends com.idega.data.IDOHome
{
 public TeeTime create() throws javax.ejb.CreateException;
 public TeeTime createLegacy();
 public TeeTime findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TeeTime findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TeeTime findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}