package is.idega.idegaweb.golf.entity;


public interface TournamentFormHome extends com.idega.data.IDOHome
{
 public TournamentForm create() throws javax.ejb.CreateException;
 public TournamentForm createLegacy();
 public TournamentForm findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentForm findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentForm findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}