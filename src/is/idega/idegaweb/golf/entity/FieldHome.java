package is.idega.idegaweb.golf.entity;


public interface FieldHome extends com.idega.data.IDOHome
{
 public Field create() throws javax.ejb.CreateException;
 public Field createLegacy();
 public Field findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Field findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Field findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findByUnion(is.idega.idegaweb.golf.entity.Union p0)throws javax.ejb.FinderException;

}