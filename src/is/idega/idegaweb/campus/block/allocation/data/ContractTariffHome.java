package is.idega.idegaweb.campus.block.allocation.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ContractTariffHome extends IDOHome {
	public ContractTariff create() throws CreateException;

	public ContractTariff findByPrimaryKey(Object pk) throws FinderException;

	public Collection findByContract(Contract contract) throws FinderException;

	public Collection findByContractTariffName(ContractTariffName name)
			throws FinderException;

	public Collection findAll() throws FinderException;
}