package is.idega.idegaweb.golf.entity;


public interface UnionImageHome extends com.idega.data.IDOHome
{
 public UnionImage create() throws javax.ejb.CreateException;
 public UnionImage createLegacy();
 public UnionImage findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public UnionImage findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public UnionImage findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}