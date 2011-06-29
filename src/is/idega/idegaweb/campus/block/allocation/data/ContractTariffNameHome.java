package is.idega.idegaweb.campus.block.allocation.data;


import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ContractTariffNameHome extends IDOHome {
	public ContractTariffName create() throws CreateException;

	public ContractTariffName findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAll() throws FinderException;

	public ContractTariffName findByContract(String name)
			throws FinderException;
}