package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class ContractRenewalServiceHomeImpl extends IBOHomeImpl implements
		ContractRenewalServiceHome {
	public Class getBeanInterfaceClass() {
		return ContractRenewalService.class;
	}

	public ContractRenewalService create() throws CreateException {
		return (ContractRenewalService) super.createIBO();
	}
}