package is.idega.idegaweb.golf.block.boxoffice.data;


public interface IssuesHome extends com.idega.data.IDOHome
{
 public Issues create() throws javax.ejb.CreateException;
 public Issues createLegacy();
 public Issues findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Issues findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Issues findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}