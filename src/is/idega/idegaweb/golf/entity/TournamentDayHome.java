package is.idega.idegaweb.golf.entity;


public interface TournamentDayHome extends com.idega.data.IDOHome
{
 public TournamentDay create() throws javax.ejb.CreateException;
 public TournamentDay createLegacy();
 public TournamentDay findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentDay findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}