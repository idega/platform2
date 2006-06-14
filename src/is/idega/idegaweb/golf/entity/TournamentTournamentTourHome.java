package is.idega.idegaweb.golf.entity;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface TournamentTournamentTourHome extends IDOHome {
	public TournamentTournamentTour create() throws CreateException;

	public TournamentTournamentTour findByPrimaryKey(TournamentTournamentTourPK primaryKey) throws FinderException;

	public TournamentTournamentTour findByPrimaryKey(Object primaryKey) throws FinderException;

	public TournamentTournamentTour create(TournamentTournamentTourPK primaryKey) throws CreateException;
}