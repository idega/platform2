package is.idega.idegaweb.golf.entity;


public interface TournamentGroupHome extends com.idega.data.IDOHome
{
 public TournamentGroup create() throws javax.ejb.CreateException;
 public TournamentGroup createLegacy();
 public TournamentGroup findByPrimaryKey(int id) throws javax.ejb.FinderException;
 public TournamentGroup findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public TournamentGroup findByPrimaryKeyLegacy(int id) throws java.sql.SQLException;

}