package is.idega.idegaweb.golf.block.news.data;


public interface NewsCategoryAttributesHome extends com.idega.data.IDOHome
{
 public NewsCategoryAttributes create() throws javax.ejb.CreateException;
 public NewsCategoryAttributes createLegacy();
 public NewsCategoryAttributes findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public NewsCategoryAttributes findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public NewsCategoryAttributes findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}