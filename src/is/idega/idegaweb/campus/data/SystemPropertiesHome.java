package is.idega.idegaweb.campus.data;


public interface SystemPropertiesHome extends com.idega.data.IDOHome
{
 public SystemProperties create() throws javax.ejb.CreateException;
 public SystemProperties createLegacy();
 public SystemProperties findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public SystemProperties findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public SystemProperties findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}