package is.idega.idegaweb.golf.entity;


public interface TournamentHome extends com.idega.data.IDOHome
{
 public Tournament create() throws javax.ejb.CreateException;
 public Tournament createLegacy();
 public Tournament findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public Tournament findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public Tournament findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}