package is.idega.idegaweb.golf.entity;


public interface GolferPageDataHome extends com.idega.data.IDOHome
{
 public GolferPageData create() throws javax.ejb.CreateException;
 public GolferPageData createLegacy();
 public GolferPageData findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public GolferPageData findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public GolferPageData findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}