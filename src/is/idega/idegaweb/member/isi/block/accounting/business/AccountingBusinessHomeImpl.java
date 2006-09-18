package is.idega.idegaweb.member.isi.block.accounting.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class AccountingBusinessHomeImpl extends IBOHomeImpl implements AccountingBusinessHome {
	public Class getBeanInterfaceClass() {
		return AccountingBusiness.class;
	}

	public AccountingBusiness create() throws CreateException {
		return (AccountingBusiness) super.createIBO();
	}
}