package is.idega.idegaweb.campus.data;


public interface BuildingAccountEntryHome extends com.idega.data.IDOHome
{
 public BuildingAccountEntry create() throws javax.ejb.CreateException;
 public BuildingAccountEntry createLegacy();
 public BuildingAccountEntry findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public BuildingAccountEntry findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public BuildingAccountEntry findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}