package is.idega.block.nationalregister.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author Joakim
 *
 */
public class NationalRegisterFileImportHandlerHomeImpl extends IBOHomeImpl implements
		NationalRegisterFileImportHandlerHome {

	protected Class getBeanInterfaceClass() {
		return NationalRegisterFileImportHandler.class;
	}

	public NationalRegisterFileImportHandler create() throws javax.ejb.CreateException {
		return (NationalRegisterFileImportHandler) super.createIBO();
	}
}
