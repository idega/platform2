package is.idega.idegaweb.golf.entity;


public interface TournamentParticipantsHome extends com.idega.data.IDOHome
{
 public TournamentParticipants create() throws javax.ejb.CreateException;
 public TournamentParticipants createLegacy();
 public TournamentParticipants findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentParticipants findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentParticipants findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}