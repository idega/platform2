package is.idega.idegaweb.golf.entity;


public interface TeeImageHome extends com.idega.data.IDOHome
{
 public TeeImage create() throws javax.ejb.CreateException;
 public TeeImage createLegacy();
 public TeeImage findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TeeImage findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TeeImage findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
}