package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class TravelSessionManagerHomeImpl extends IBOHomeImpl implements TravelSessionManagerHome {

	protected Class getBeanInterfaceClass() {
		return TravelSessionManager.class;
	}

	public TravelSessionManager create() throws javax.ejb.CreateException {
		return (TravelSessionManager) super.createIBO();
	}
}
