package is.idega.idegaweb.golf.entity;


public interface FamilyHome extends com.idega.data.IDOHome
{
 public Family create() throws javax.ejb.CreateException;
 public Family createLegacy();
 public Family findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Family findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Family findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}