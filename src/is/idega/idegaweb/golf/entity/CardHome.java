package is.idega.idegaweb.golf.entity;


public interface CardHome extends com.idega.data.IDOHome
{
 public Card create() throws javax.ejb.CreateException;
 public Card createLegacy();
 public Card findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Card findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Card findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}