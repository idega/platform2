package is.idega.idegaweb.golf.entity;


public interface DismissalHome extends com.idega.data.IDOHome
{
 public Dismissal create() throws javax.ejb.CreateException;
 public Dismissal createLegacy();
 public Dismissal findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Dismissal findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Dismissal findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}