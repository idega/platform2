package is.idega.idegaweb.golf.entity;


public interface StartingtimeViewHome extends com.idega.data.IDOHome
{
 public StartingtimeView create() throws javax.ejb.CreateException;
 public StartingtimeView createLegacy();
 public StartingtimeView findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public StartingtimeView findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public StartingtimeView findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}