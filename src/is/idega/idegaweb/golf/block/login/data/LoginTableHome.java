package is.idega.idegaweb.golf.block.login.data;


public interface LoginTableHome extends com.idega.data.IDOHome
{
 public LoginTable create() throws javax.ejb.CreateException;
 public LoginTable createLegacy();
 public LoginTable findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public LoginTable findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public LoginTable findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;
 public LoginTable findByMember(is.idega.idegaweb.golf.entity.Member p0)throws javax.ejb.EJBException,javax.ejb.FinderException;
 public java.util.Collection findByUserLogin(java.lang.String p0)throws javax.ejb.FinderException;

}