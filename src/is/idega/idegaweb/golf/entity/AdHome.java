package is.idega.idegaweb.golf.entity;


public interface AdHome extends com.idega.data.IDOHome
{
 public Ad create() throws javax.ejb.CreateException;
 public Ad createLegacy();
 public Ad findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Ad findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Ad findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}