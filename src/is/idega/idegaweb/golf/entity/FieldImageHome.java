package is.idega.idegaweb.golf.entity;


public interface FieldImageHome extends com.idega.data.IDOHome
{
 public FieldImage create() throws javax.ejb.CreateException;
 public FieldImage createLegacy();
 public FieldImage findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public FieldImage findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public FieldImage findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
}