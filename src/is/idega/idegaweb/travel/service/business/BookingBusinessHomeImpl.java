package is.idega.idegaweb.travel.service.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class BookingBusinessHomeImpl extends IBOHomeImpl implements BookingBusinessHome {

	protected Class getBeanInterfaceClass() {
		return BookingBusiness.class;
	}

	public BookingBusiness create() throws javax.ejb.CreateException {
		return (BookingBusiness) super.createIBO();
	}
}
