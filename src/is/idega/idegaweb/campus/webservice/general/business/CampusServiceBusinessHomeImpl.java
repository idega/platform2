package is.idega.idegaweb.campus.webservice.general.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class CampusServiceBusinessHomeImpl extends IBOHomeImpl implements CampusServiceBusinessHome {
	public Class getBeanInterfaceClass() {
		return CampusServiceBusiness.class;
	}

	public CampusServiceBusiness create() throws CreateException {
		return (CampusServiceBusiness) super.createIBO();
	}
}