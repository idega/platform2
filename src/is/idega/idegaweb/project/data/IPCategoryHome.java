package is.idega.idegaweb.project.data;


public interface IPCategoryHome extends com.idega.data.IDOHome
{
 public IPCategory create() throws javax.ejb.CreateException;
 public IPCategory createLegacy();
 public IPCategory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public IPCategory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IPCategory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}