package is.idega.idegaweb.golf.entity;


public interface TournamentGroupRoundDayHome extends com.idega.data.IDOHome
{
 public TournamentGroupRoundDay create() throws javax.ejb.CreateException;
 public TournamentGroupRoundDay createLegacy();
 public TournamentGroupRoundDay findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentGroupRoundDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentGroupRoundDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}