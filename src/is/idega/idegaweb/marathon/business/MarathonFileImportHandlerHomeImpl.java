package is.idega.idegaweb.marathon.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class MarathonFileImportHandlerHomeImpl extends IBOHomeImpl implements MarathonFileImportHandlerHome {

	protected Class getBeanInterfaceClass() {
		return MarathonFileImportHandler.class;
	}

	public MarathonFileImportHandler create() throws javax.ejb.CreateException {
		return (MarathonFileImportHandler) super.createIBO();
	}
}
