package is.idega.idegaweb.golf.entity;


public interface TournamentTypeHome extends com.idega.data.IDOHome
{
 public TournamentType create() throws javax.ejb.CreateException;
 public TournamentType createLegacy();
 public TournamentType findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentType findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}