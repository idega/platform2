package is.idega.idegaweb.golf.entity;


public interface AccountEntryHome extends com.idega.data.IDOHome
{
 public AccountEntry create() throws javax.ejb.CreateException;
 public AccountEntry createLegacy();
 public AccountEntry findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AccountEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}