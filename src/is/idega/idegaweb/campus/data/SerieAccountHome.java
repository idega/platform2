package is.idega.idegaweb.campus.data;


public interface SerieAccountHome extends com.idega.data.IDOHome
{
 public SerieAccount create() throws javax.ejb.CreateException;
 public SerieAccount createLegacy();
 public SerieAccount findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public SerieAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public SerieAccount findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}