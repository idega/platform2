package is.idega.idegaweb.golf.entity;


public interface UnionTextHome extends com.idega.data.IDOHome
{
 public UnionText create() throws javax.ejb.CreateException;
 public UnionText createLegacy();
 public UnionText findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public UnionText findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public UnionText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}