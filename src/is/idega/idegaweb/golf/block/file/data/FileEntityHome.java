package is.idega.idegaweb.golf.block.file.data;


public interface FileEntityHome extends com.idega.data.IDOHome
{
 public FileEntity create() throws javax.ejb.CreateException;
 public FileEntity createLegacy();
 public FileEntity findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public FileEntity findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public FileEntity findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}