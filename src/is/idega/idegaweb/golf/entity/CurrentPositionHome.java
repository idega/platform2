package is.idega.idegaweb.golf.entity;


public interface CurrentPositionHome extends com.idega.data.IDOHome
{
 public CurrentPosition create() throws javax.ejb.CreateException;
 public CurrentPosition createLegacy();
 public CurrentPosition findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CurrentPosition findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CurrentPosition findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}