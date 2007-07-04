package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;

import com.idega.business.IBOHomeImpl;

public class CampusApplicationsImportHandlerHomeImpl extends IBOHomeImpl implements CampusApplicationsImportHandlerHome {
	public Class getBeanInterfaceClass() {
		return CampusApplicationsImportHandler.class;
	}

	public CampusApplicationsImportHandler create() throws CreateException {
		return (CampusApplicationsImportHandler) super.createIBO();
	}
}