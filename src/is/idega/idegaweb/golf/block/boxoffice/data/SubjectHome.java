package is.idega.idegaweb.golf.block.boxoffice.data;


public interface SubjectHome extends com.idega.data.IDOHome
{
 public Subject create() throws javax.ejb.CreateException;
 public Subject createLegacy();
 public Subject findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Subject findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Subject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}