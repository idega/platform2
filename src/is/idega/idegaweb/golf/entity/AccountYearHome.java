package is.idega.idegaweb.golf.entity;


public interface AccountYearHome extends com.idega.data.IDOHome
{
 public AccountYear create() throws javax.ejb.CreateException;
 public AccountYear createLegacy();
 public AccountYear findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountYear findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}