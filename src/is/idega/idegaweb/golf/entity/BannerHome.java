package is.idega.idegaweb.golf.entity;


public interface BannerHome extends com.idega.data.IDOHome
{
 public Banner create() throws javax.ejb.CreateException;
 public Banner createLegacy();
 public Banner findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Banner findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Banner findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}