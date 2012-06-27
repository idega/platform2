package is.idega.idegaweb.campus.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ContractRenewalOfferHomeImpl extends IDOFactory implements
		ContractRenewalOfferHome {
	public Class getEntityInterfaceClass() {
		return ContractRenewalOffer.class;
	}

	public ContractRenewalOffer create() throws CreateException {
		return (ContractRenewalOffer) super.createIDO();
	}

	public ContractRenewalOffer findByPrimaryKey(Object pk)
			throws FinderException {
		return (ContractRenewalOffer) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ContractRenewalOfferBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllOpen() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ContractRenewalOfferBMPBean) entity)
				.ejbFindAllOpen();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllUnanswered() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ContractRenewalOfferBMPBean) entity)
				.ejbFindAllUnanswered();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public ContractRenewalOffer findByUUID(String uuid, boolean showClosed)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ContractRenewalOfferBMPBean) entity).ejbFindByUUID(uuid,
				showClosed);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}