package is.idega.idegaweb.campus.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CampusServiceHomeImpl extends IBOHomeImpl implements
		CampusServiceHome {
	public Class getBeanInterfaceClass() {
		return CampusService.class;
	}

	public CampusService create() throws CreateException {
		return (CampusService) super.createIBO();
	}
}