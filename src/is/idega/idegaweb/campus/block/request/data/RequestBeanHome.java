package is.idega.idegaweb.campus.block.request.data;


public interface RequestBeanHome extends com.idega.data.IDOHome
{
 public RequestBean create() throws javax.ejb.CreateException;
 public RequestBean createLegacy();
 public RequestBean findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public RequestBean findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public RequestBean findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}