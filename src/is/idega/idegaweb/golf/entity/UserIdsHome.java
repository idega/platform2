package is.idega.idegaweb.golf.entity;


public interface UserIdsHome extends com.idega.data.IDOHome
{
 public UserIds create() throws javax.ejb.CreateException;
 public UserIds createLegacy();
 public UserIds findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public UserIds findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public UserIds findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}