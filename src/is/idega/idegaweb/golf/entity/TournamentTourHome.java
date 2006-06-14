package is.idega.idegaweb.golf.entity;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface TournamentTourHome extends IDOHome {
	public TournamentTour create() throws CreateException;

	public TournamentTour findByPrimaryKey(Object pk) throws FinderException;

	public Collection findAllByUnionID(int unionID) throws FinderException;

	public Collection findByTournamentID(Object tournamentID) throws FinderException, IDORelationshipException;

	public Collection getTournamentIDs(TournamentTour tour) throws FinderException;
}