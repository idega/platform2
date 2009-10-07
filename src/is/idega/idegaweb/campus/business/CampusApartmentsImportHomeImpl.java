package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CampusApartmentsImportHomeImpl extends IBOHomeImpl implements
		CampusApartmentsImportHome {
	public Class getBeanInterfaceClass() {
		return CampusApartmentsImport.class;
	}

	public CampusApartmentsImport create() throws CreateException {
		return (CampusApartmentsImport) super.createIBO();
	}
}