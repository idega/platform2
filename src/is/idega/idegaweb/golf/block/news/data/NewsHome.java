package is.idega.idegaweb.golf.block.news.data;


public interface NewsHome extends com.idega.data.IDOHome
{
 public News create() throws javax.ejb.CreateException;
 public News createLegacy();
 public News findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public News findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public News findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}