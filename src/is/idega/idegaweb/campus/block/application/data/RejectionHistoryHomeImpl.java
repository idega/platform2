package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.block.application.data.Application;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class RejectionHistoryHomeImpl extends IDOFactory implements
		RejectionHistoryHome {
	public Class getEntityInterfaceClass() {
		return RejectionHistory.class;
	}

	public RejectionHistory create() throws CreateException {
		return (RejectionHistory) super.createIDO();
	}

	public RejectionHistory findByPrimaryKey(Object pk) throws FinderException {
		return (RejectionHistory) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByApplication(Application application)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((RejectionHistoryBMPBean) entity)
				.ejbFindAllByApplication(application);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}