package is.idega.idegaweb.golf.entity;


public interface TeeColorHome extends com.idega.data.IDOHome
{
 public TeeColor create() throws javax.ejb.CreateException;
 public TeeColor createLegacy();
 public TeeColor findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TeeColor findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TeeColor findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}