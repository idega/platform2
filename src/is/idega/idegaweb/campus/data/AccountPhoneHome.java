package is.idega.idegaweb.campus.data;


public interface AccountPhoneHome extends com.idega.data.IDOHome
{
 public AccountPhone create() throws javax.ejb.CreateException;
 public AccountPhone createLegacy();
 public AccountPhone findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountPhone findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}