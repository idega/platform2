package is.idega.idegaweb.golf.entity;


public interface PriceCatalogueHome extends com.idega.data.IDOHome
{
 public PriceCatalogue create() throws javax.ejb.CreateException;
 public PriceCatalogue createLegacy();
 public PriceCatalogue findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public PriceCatalogue findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public PriceCatalogue findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}