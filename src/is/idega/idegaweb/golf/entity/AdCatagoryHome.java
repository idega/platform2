package is.idega.idegaweb.golf.entity;


public interface AdCatagoryHome extends com.idega.data.IDOHome
{
 public AdCatagory create() throws javax.ejb.CreateException;
 public AdCatagory createLegacy();
 public AdCatagory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public AdCatagory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public AdCatagory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}