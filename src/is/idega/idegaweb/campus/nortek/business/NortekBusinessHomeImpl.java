package is.idega.idegaweb.campus.nortek.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class NortekBusinessHomeImpl extends IBOHomeImpl implements
		NortekBusinessHome {
	public Class getBeanInterfaceClass() {
		return NortekBusiness.class;
	}

	public NortekBusiness create() throws CreateException {
		return (NortekBusiness) super.createIBO();
	}
}