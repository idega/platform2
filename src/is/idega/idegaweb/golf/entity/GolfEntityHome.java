package is.idega.idegaweb.golf.entity;


public interface GolfEntityHome extends com.idega.data.IDOHome
{
 public GolfEntity create() throws javax.ejb.CreateException;
 public GolfEntity createLegacy();
 public GolfEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public GolfEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public GolfEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}