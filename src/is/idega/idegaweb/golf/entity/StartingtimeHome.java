package is.idega.idegaweb.golf.entity;


public interface StartingtimeHome extends com.idega.data.IDOHome
{
 public Startingtime create() throws javax.ejb.CreateException;
 public Startingtime createLegacy();
 public Startingtime findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Startingtime findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Startingtime findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}