package is.idega.idegaweb.intershop.data;


public interface IShopTemplateBeanHome extends com.idega.data.IDOHome
{
 public IShopTemplateBean create() throws javax.ejb.CreateException;
 public IShopTemplateBean createLegacy();
 public IShopTemplateBean findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public IShopTemplateBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public IShopTemplateBean findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}