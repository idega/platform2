package is.idega.idegaweb.travel.block.search.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class ServiceSearchBusinessHomeImpl extends IBOHomeImpl implements ServiceSearchBusinessHome {

	protected Class getBeanInterfaceClass() {
		return ServiceSearchBusiness.class;
	}

	public ServiceSearchBusiness create() throws javax.ejb.CreateException {
		return (ServiceSearchBusiness) super.createIBO();
	}
}
