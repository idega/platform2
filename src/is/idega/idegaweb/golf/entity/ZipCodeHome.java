package is.idega.idegaweb.golf.entity;


public interface ZipCodeHome extends com.idega.data.IDOHome
{
 public ZipCode create() throws javax.ejb.CreateException;
 public ZipCode createLegacy();
 public ZipCode findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ZipCode findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public ZipCode findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public ZipCode findByCode(java.lang.String p0)throws javax.ejb.FinderException;

}