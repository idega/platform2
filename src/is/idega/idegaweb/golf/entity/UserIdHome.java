package is.idega.idegaweb.golf.entity;


public interface UserIdHome extends com.idega.data.IDOHome
{
 public UserId create() throws javax.ejb.CreateException;
 public UserId createLegacy();
 public UserId findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public UserId findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public UserId findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}