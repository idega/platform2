package is.idega.idegaweb.golf.entity;


public interface TournamentRoundHome extends com.idega.data.IDOHome
{
 public TournamentRound create() throws javax.ejb.CreateException;
 public TournamentRound createLegacy();
 public TournamentRound findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentRound findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentRound findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}