package is.idega.idegaweb.travel.data;


public interface ServiceHome extends com.idega.data.IDOHome
{
 public Service create() throws javax.ejb.CreateException;
 public Service createLegacy();
 public Service findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Service findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Service findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}