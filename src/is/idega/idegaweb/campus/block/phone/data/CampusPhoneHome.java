package is.idega.idegaweb.campus.block.phone.data;


public interface CampusPhoneHome extends com.idega.data.IDOHome
{
 public CampusPhone create() throws javax.ejb.CreateException;
 public CampusPhone createLegacy();
 public CampusPhone findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CampusPhone findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CampusPhone findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}