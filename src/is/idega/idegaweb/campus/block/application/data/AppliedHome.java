package is.idega.idegaweb.campus.block.application.data;


public interface AppliedHome extends com.idega.data.IDOHome
{
 public Applied create() throws javax.ejb.CreateException;
 public Applied createLegacy();
 public Applied findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Applied findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Applied findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}