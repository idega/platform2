package is.idega.block.nationalregister.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author Joakim
 *
 */
public class NationalRegisterBusinessHomeImpl extends IBOHomeImpl implements NationalRegisterBusinessHome {

	protected Class getBeanInterfaceClass() {
		return NationalRegisterBusiness.class;
	}

	public NationalRegisterBusiness create() throws javax.ejb.CreateException {
		return (NationalRegisterBusiness) super.createIBO();
	}
}
