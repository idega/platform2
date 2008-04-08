package is.idega.idegaweb.campus.block.allocation.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class ContractServiceHomeImpl extends IBOHomeImpl implements
		ContractServiceHome {
	public Class getBeanInterfaceClass() {
		return ContractService.class;
	}

	public ContractService create() throws CreateException {
		return (ContractService) super.createIBO();
	}
}