package is.idega.idegaweb.travel.business;

import com.idega.business.IBOHomeImpl;


/**
 * @author gimmi
 */
public class BookerHomeImpl extends IBOHomeImpl implements BookerHome {

	protected Class getBeanInterfaceClass() {
		return Booker.class;
	}

	public Booker create() throws javax.ejb.CreateException {
		return (Booker) super.createIBO();
	}
}
