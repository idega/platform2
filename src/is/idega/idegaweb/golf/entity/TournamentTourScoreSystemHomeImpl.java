package is.idega.idegaweb.golf.entity;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class TournamentTourScoreSystemHomeImpl extends IDOFactory implements TournamentTourScoreSystemHome{
	public Class getEntityInterfaceClass() {
		return TournamentTourScoreSystem.class;
	}

	public TournamentTourScoreSystem create() throws CreateException {
		return (TournamentTourScoreSystem) super.createIDO();
	}

	public TournamentTourScoreSystem findByPrimaryKey(Object pk) throws FinderException {
		return (TournamentTourScoreSystem) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((TournamentTourScoreSystemBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}