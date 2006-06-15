package is.idega.idegaweb.golf.entity;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class TournamentTourMemberHomeImpl extends IDOFactory implements TournamentTourMemberHome {
	public Class getEntityInterfaceClass() {
		return TournamentTourMember.class;
	}

	public TournamentTourMember create() throws CreateException {
		return (TournamentTourMember) super.createIDO();
	}

	public TournamentTourMember findByPrimaryKey(Object pk) throws FinderException {
		return (TournamentTourMember) super.findByPrimaryKeyIDO(pk);
	}

	public TournamentTourMember findByPrimaryKey(TournamentTourMemberPK primaryKey) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((TournamentTourMemberBMPBean) entity).ejbFindByPrimaryKey(primaryKey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public TournamentTourMember create(TournamentTourMemberPK primaryKey) throws CreateException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((TournamentTourMemberBMPBean) entity).ejbCreate(primaryKey);
//		((TournamentTourMemberBMPBean) entity).ejbPostCreate();
		this.idoCheckInPooledEntity(entity);
		try {
			return findByPrimaryKey(pk);
		} catch (FinderException fe) {
			throw new IDOCreateException(fe);
		} catch (Exception e) {
			throw new IDOCreateException(e);
		}
	}

	public int[] getTournamentGroupsInUse(TournamentTour tour) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		int[] theReturn = ((TournamentTourMemberBMPBean) entity).ejbHomeGetTournamentGroupsInUse(tour);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

	public Collection getScoresOrdered(TournamentTour tour, Collection tournamentPKs, Collection tournamentGroupPKs) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection theReturn = ((TournamentTourMemberBMPBean) entity).ejbHomeGetScoresOrdered(tour, tournamentPKs, tournamentGroupPKs);
		this.idoCheckInPooledEntity(entity);
		return theReturn;
	}

}