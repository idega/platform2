package is.idega.idegaweb.golf.tournament.data;


public interface TournamentHome extends com.idega.data.IDOHome
{
 public Tournament create() throws javax.ejb.CreateException;
 public Tournament findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}