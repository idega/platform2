package is.idega.idegaweb.campus.block.phone.data;


public interface PhoneFileInfoHome extends com.idega.data.IDOHome
{
 public PhoneFileInfo create() throws javax.ejb.CreateException;
 public PhoneFileInfo createLegacy();
 public PhoneFileInfo findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PhoneFileInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PhoneFileInfo findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}