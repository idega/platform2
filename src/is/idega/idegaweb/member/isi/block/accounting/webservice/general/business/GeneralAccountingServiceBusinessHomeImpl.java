package is.idega.idegaweb.member.isi.block.accounting.webservice.general.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class GeneralAccountingServiceBusinessHomeImpl extends IBOHomeImpl implements GeneralAccountingServiceBusinessHome {
	public Class getBeanInterfaceClass() {
		return GeneralAccountingServiceBusiness.class;
	}

	public GeneralAccountingServiceBusiness create() throws CreateException {
		return (GeneralAccountingServiceBusiness) super.createIBO();
	}
}