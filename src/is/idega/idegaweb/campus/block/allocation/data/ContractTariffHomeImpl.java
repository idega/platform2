package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ContractTariffHomeImpl extends IDOFactory implements
		ContractTariffHome {
	public Class getEntityInterfaceClass() {
		return ContractTariff.class;
	}

	public ContractTariff create() throws CreateException {
		return (ContractTariff) super.createIDO();
	}

	public ContractTariff findByPrimaryKey(Object pk) throws FinderException {
		return (ContractTariff) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByContract(Contract contract) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ContractTariffBMPBean) entity)
				.ejbFindByContract(contract);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByContractTariffName(ContractTariffName name)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ContractTariffBMPBean) entity)
				.ejbFindByContractTariffName(name);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ContractTariffBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}