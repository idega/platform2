package is.idega.block.nationalregister.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class NationalRegisterDeceasedBusinessHomeImpl extends IBOHomeImpl implements NationalRegisterDeceasedBusinessHome {

	public Class getBeanInterfaceClass() {
		return NationalRegisterDeceasedBusiness.class;
	}

	public NationalRegisterDeceasedBusiness create() throws CreateException {
		return (NationalRegisterDeceasedBusiness) super.createIBO();
	}
}