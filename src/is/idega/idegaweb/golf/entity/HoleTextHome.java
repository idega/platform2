package is.idega.idegaweb.golf.entity;


public interface HoleTextHome extends com.idega.data.IDOHome
{
 public HoleText create() throws javax.ejb.CreateException;
 public HoleText createLegacy();
 public HoleText findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public HoleText findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public HoleText findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
}