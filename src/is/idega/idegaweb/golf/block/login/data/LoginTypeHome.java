package is.idega.idegaweb.golf.block.login.data;


public interface LoginTypeHome extends com.idega.data.IDOHome
{
 public LoginType create() throws javax.ejb.CreateException;
 public LoginType createLegacy();
 public LoginType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public LoginType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public LoginType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}