package is.idega.idegaweb.golf.entity;


public interface PollHome extends com.idega.data.IDOHome
{
 public Poll create() throws javax.ejb.CreateException;
 public Poll createLegacy();
 public Poll findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Poll findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Poll findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}