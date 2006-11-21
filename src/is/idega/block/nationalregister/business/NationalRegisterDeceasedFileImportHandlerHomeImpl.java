package is.idega.block.nationalregister.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class NationalRegisterDeceasedFileImportHandlerHomeImpl extends IBOHomeImpl implements NationalRegisterDeceasedFileImportHandlerHome {

	public Class getBeanInterfaceClass() {
		return NationalRegisterDeceasedFileImportHandler.class;
	}

	public NationalRegisterDeceasedFileImportHandler create() throws CreateException {
		return (NationalRegisterDeceasedFileImportHandler) super.createIBO();
	}
}