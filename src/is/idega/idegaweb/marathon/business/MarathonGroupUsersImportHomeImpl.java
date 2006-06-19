package is.idega.idegaweb.marathon.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class MarathonGroupUsersImportHomeImpl extends IBOHomeImpl implements MarathonGroupUsersImportHome {

	public Class getBeanInterfaceClass() {
		return MarathonGroupUsersImport.class;
	}

	public MarathonGroupUsersImport create() throws CreateException {
		return (MarathonGroupUsersImport) super.createIBO();
	}
}