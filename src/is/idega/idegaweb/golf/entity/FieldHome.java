package is.idega.idegaweb.golf.entity;


public interface FieldHome extends com.idega.data.IDOHome
{
 public Field create() throws javax.ejb.CreateException;
 public Field createLegacy();
 public Field findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Field findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Field findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}