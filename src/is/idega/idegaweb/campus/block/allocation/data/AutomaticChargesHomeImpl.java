package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.block.finance.data.AssessmentRound;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.user.data.User;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class AutomaticChargesHomeImpl extends IDOFactory implements
		AutomaticChargesHome {
	public Class getEntityInterfaceClass() {
		return AutomaticCharges.class;
	}

	public AutomaticCharges create() throws CreateException {
		return (AutomaticCharges) super.createIDO();
	}

	public AutomaticCharges findByPrimaryKey(Object pk) throws FinderException {
		return (AutomaticCharges) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AutomaticChargesBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public AutomaticCharges findByUser(User user) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((AutomaticChargesBMPBean) entity).ejbFindByUser(user);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}

	public Collection findTransferByAssessmentRound(AssessmentRound round)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AutomaticChargesBMPBean) entity)
				.ejbFindTransferByAssessmentRound(round);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHandlingByAssessmentRound(AssessmentRound round)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AutomaticChargesBMPBean) entity)
				.ejbFindHandlingByAssessmentRound(round);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findTransferByAssessmentRound(Integer roundID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AutomaticChargesBMPBean) entity)
				.ejbFindTransferByAssessmentRound(roundID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findHandlingByAssessmentRound(Integer roundID)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((AutomaticChargesBMPBean) entity)
				.ejbFindHandlingByAssessmentRound(roundID);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}