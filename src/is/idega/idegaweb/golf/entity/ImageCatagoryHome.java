package is.idega.idegaweb.golf.entity;


public interface ImageCatagoryHome extends com.idega.data.IDOHome
{
 public ImageCatagory create() throws javax.ejb.CreateException;
 public ImageCatagory createLegacy();
 public ImageCatagory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ImageCatagory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ImageCatagory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}