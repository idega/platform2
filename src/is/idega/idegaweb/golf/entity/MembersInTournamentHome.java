package is.idega.idegaweb.golf.entity;


public interface MembersInTournamentHome extends com.idega.data.IDOHome
{
 public MembersInTournament create() throws javax.ejb.CreateException;
 public MembersInTournament createLegacy();
 public MembersInTournament findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public MembersInTournament findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public MembersInTournament findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}