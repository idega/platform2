package is.idega.idegaweb.campus.webservice.building.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class BuildingWSServiceBusinessHomeImpl extends IBOHomeImpl implements
		BuildingWSServiceBusinessHome {
	public Class getBeanInterfaceClass() {
		return BuildingWSServiceBusiness.class;
	}

	public BuildingWSServiceBusiness create() throws CreateException {
		return (BuildingWSServiceBusiness) super.createIBO();
	}
}