package is.idega.idegaweb.golf.entity;


public interface StartingtimeFieldConfigHome extends com.idega.data.IDOHome
{
 public StartingtimeFieldConfig create() throws javax.ejb.CreateException;
 public StartingtimeFieldConfig createLegacy();
 public StartingtimeFieldConfig findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StartingtimeFieldConfig findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StartingtimeFieldConfig findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}