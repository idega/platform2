package is.idega.idegaweb.golf.entity;


public interface StartingtimeFieldConfigHome extends com.idega.data.IDOHome
{
 public StartingtimeFieldConfig create() throws javax.ejb.CreateException;
 public StartingtimeFieldConfig createLegacy();
 public StartingtimeFieldConfig findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StartingtimeFieldConfig findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StartingtimeFieldConfig findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAllActiveTeetimeFieldConfigurations(com.idega.util.IWTimestamp p0)throws javax.ejb.FinderException;

}