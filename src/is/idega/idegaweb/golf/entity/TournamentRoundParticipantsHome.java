package is.idega.idegaweb.golf.entity;


public interface TournamentRoundParticipantsHome extends com.idega.data.IDOHome
{
 public TournamentRoundParticipants create() throws javax.ejb.CreateException;
 public TournamentRoundParticipants createLegacy();
 public TournamentRoundParticipants findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentRoundParticipants findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentRoundParticipants findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}