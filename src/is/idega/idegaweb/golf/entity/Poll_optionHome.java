package is.idega.idegaweb.golf.entity;


public interface Poll_optionHome extends com.idega.data.IDOHome
{
 public Poll_option create() throws javax.ejb.CreateException;
 public Poll_option createLegacy();
 public Poll_option findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Poll_option findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Poll_option findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}