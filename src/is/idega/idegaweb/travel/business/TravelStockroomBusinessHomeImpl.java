package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class TravelStockroomBusinessHomeImpl extends IBOHomeImpl implements TravelStockroomBusinessHome {

	protected Class getBeanInterfaceClass() {
		return TravelStockroomBusiness.class;
	}

	public TravelStockroomBusiness create() throws javax.ejb.CreateException {
		return (TravelStockroomBusiness) super.createIBO();
	}
}
