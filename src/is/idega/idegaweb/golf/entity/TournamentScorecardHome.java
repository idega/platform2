package is.idega.idegaweb.golf.entity;


public interface TournamentScorecardHome extends com.idega.data.IDOHome
{
 public TournamentScorecard create() throws javax.ejb.CreateException;
 public TournamentScorecard createLegacy();
 public TournamentScorecard findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentScorecard findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentScorecard findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}