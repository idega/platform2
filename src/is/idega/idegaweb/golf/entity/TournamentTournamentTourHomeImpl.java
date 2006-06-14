package is.idega.idegaweb.golf.entity;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOCreateException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class TournamentTournamentTourHomeImpl extends IDOFactory implements TournamentTournamentTourHome{
	public Class getEntityInterfaceClass() {
		return TournamentTournamentTour.class;
	}

	public TournamentTournamentTour create() throws CreateException {
		return (TournamentTournamentTour) super.createIDO();
	}

	public TournamentTournamentTour findByPrimaryKey(Object pk) throws FinderException {
		return (TournamentTournamentTour) super.findByPrimaryKeyIDO(pk);
	}

	public TournamentTournamentTour findByPrimaryKey(TournamentTournamentTourPK primaryKey) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((TournamentTournamentTourBMPBean) entity).ejbFindByPrimaryKey(primaryKey);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public TournamentTournamentTour create(TournamentTournamentTourPK primaryKey) throws CreateException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((TournamentTournamentTourBMPBean) entity).ejbCreate(primaryKey);
		//((TournamentTournamentTourBMPBean) entity).ejbPostCreate();
		this.idoCheckInPooledEntity(entity);
		try {
			return findByPrimaryKey(pk);
		} catch (FinderException fe) {
			throw new IDOCreateException(fe);
		} catch (Exception e) {
			throw new IDOCreateException(e);
		}
	}
}