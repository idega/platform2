package is.idega.idegaweb.golf.entity;


public interface Poll_resultHome extends com.idega.data.IDOHome
{
 public Poll_result create() throws javax.ejb.CreateException;
 public Poll_result createLegacy();
 public Poll_result findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Poll_result findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Poll_result findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}