package is.idega.idegaweb.golf.block.image.data;


public interface ImageCatagoryAttributesHome extends com.idega.data.IDOHome
{
 public ImageCatagoryAttributes create() throws javax.ejb.CreateException;
 public ImageCatagoryAttributes createLegacy();
 public ImageCatagoryAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ImageCatagoryAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ImageCatagoryAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}