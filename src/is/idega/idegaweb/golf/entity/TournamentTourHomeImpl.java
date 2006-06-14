package is.idega.idegaweb.golf.entity;


import com.idega.data.IDORelationshipException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class TournamentTourHomeImpl extends IDOFactory implements TournamentTourHome {
	public Class getEntityInterfaceClass() {
		return TournamentTour.class;
	}

	public TournamentTour create() throws CreateException {
		return (TournamentTour) super.createIDO();
	}

	public TournamentTour findByPrimaryKey(Object pk) throws FinderException {
		return (TournamentTour) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByUnionID(int unionID) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TournamentTourBMPBean) entity).ejbFindAllByUnionID(unionID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByTournamentID(Object tournamentID) throws FinderException, IDORelationshipException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TournamentTourBMPBean) entity).ejbFindByTournamentID(tournamentID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection getTournamentIDs(TournamentTour tour) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((TournamentTourBMPBean) entity).ejbHomeGetTournamentIDs(tour);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}
}