package is.idega.idegaweb.project.data;


public interface IPProjectHome extends com.idega.data.IDOHome
{
 public IPProject create() throws javax.ejb.CreateException;
 public IPProject createLegacy();
 public IPProject findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public IPProject findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IPProject findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}