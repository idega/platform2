package is.idega.idegaweb.golf.block.text.data;


public interface TextModuleHome extends com.idega.data.IDOHome
{
 public TextModule create() throws javax.ejb.CreateException;
 public TextModule createLegacy();
 public TextModule findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TextModule findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TextModule findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}