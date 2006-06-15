package is.idega.idegaweb.golf.entity;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface TournamentTourMemberHome extends IDOHome {
	public TournamentTourMember create() throws CreateException;

	public TournamentTourMember findByPrimaryKey(TournamentTourMemberPK primaryKey) throws FinderException;

	public TournamentTourMember findByPrimaryKey(Object primaryKey) throws FinderException;

	public TournamentTourMember create(TournamentTourMemberPK primaryKey) throws CreateException;

	public int[] getTournamentGroupsInUse(TournamentTour tour) throws FinderException;

	public Collection getScoresOrdered(TournamentTour tour, Collection tournamentPKs, Collection tournamentGroupPKs) throws FinderException;
}