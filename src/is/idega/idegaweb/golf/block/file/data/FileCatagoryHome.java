package is.idega.idegaweb.golf.block.file.data;


public interface FileCatagoryHome extends com.idega.data.IDOHome
{
 public FileCatagory create() throws javax.ejb.CreateException;
 public FileCatagory createLegacy();
 public FileCatagory findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public FileCatagory findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public FileCatagory findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}