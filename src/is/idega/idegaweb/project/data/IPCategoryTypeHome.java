package is.idega.idegaweb.project.data;


public interface IPCategoryTypeHome extends com.idega.data.IDOHome
{
 public IPCategoryType create() throws javax.ejb.CreateException;
 public IPCategoryType createLegacy();
 public IPCategoryType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public IPCategoryType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IPCategoryType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}