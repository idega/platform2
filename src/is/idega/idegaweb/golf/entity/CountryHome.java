package is.idega.idegaweb.golf.entity;


public interface CountryHome extends com.idega.data.IDOHome
{
 public Country create() throws javax.ejb.CreateException;
 public Country createLegacy();
 public Country findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Country findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Country findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public Country findByAbbreviation(java.lang.String p0)throws javax.ejb.FinderException;

}