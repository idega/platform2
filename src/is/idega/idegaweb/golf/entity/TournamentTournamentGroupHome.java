package is.idega.idegaweb.golf.entity;


public interface TournamentTournamentGroupHome extends com.idega.data.IDOHome
{
 public TournamentTournamentGroup create() throws javax.ejb.CreateException;
 public TournamentTournamentGroup createLegacy();
 public TournamentTournamentGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentTournamentGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentTournamentGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}