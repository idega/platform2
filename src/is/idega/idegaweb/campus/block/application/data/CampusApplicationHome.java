package is.idega.idegaweb.campus.block.application.data;


public interface CampusApplicationHome extends com.idega.data.IDOHome
{
 public CampusApplication create() throws javax.ejb.CreateException;
 public CampusApplication createLegacy();
 public CampusApplication findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CampusApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CampusApplication findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}