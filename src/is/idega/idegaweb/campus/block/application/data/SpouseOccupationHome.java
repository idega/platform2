package is.idega.idegaweb.campus.block.application.data;


public interface SpouseOccupationHome extends com.idega.data.IDOHome
{
 public SpouseOccupation create() throws javax.ejb.CreateException;
 public SpouseOccupation createLegacy();
 public SpouseOccupation findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public SpouseOccupation findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public SpouseOccupation findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}