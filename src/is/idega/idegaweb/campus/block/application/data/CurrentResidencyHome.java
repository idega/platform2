package is.idega.idegaweb.campus.block.application.data;


public interface CurrentResidencyHome extends com.idega.data.IDOHome
{
 public CurrentResidency create() throws javax.ejb.CreateException;
 public CurrentResidency createLegacy();
 public CurrentResidency findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public CurrentResidency findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public CurrentResidency findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}