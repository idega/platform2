package is.idega.idegaweb.campus.block.allocation.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class ContractTariffNameHomeImpl extends IDOFactory implements
		ContractTariffNameHome {
	public Class getEntityInterfaceClass() {
		return ContractTariffName.class;
	}

	public ContractTariffName create() throws CreateException {
		return (ContractTariffName) super.createIDO();
	}

	public ContractTariffName findByPrimaryKey(Object pk)
			throws FinderException {
		return (ContractTariffName) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ContractTariffNameBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public ContractTariffName findByContract(String name)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ContractTariffNameBMPBean) entity)
				.ejbFindByContract(name);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}