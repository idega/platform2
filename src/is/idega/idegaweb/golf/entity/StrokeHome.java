package is.idega.idegaweb.golf.entity;


public interface StrokeHome extends com.idega.data.IDOHome
{
 public Stroke create() throws javax.ejb.CreateException;
 public Stroke createLegacy();
 public Stroke findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Stroke findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Stroke findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}