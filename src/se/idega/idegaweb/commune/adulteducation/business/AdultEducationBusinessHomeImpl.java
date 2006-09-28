package se.idega.idegaweb.commune.adulteducation.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class AdultEducationBusinessHomeImpl extends IBOHomeImpl implements AdultEducationBusinessHome {
	public Class getBeanInterfaceClass() {
		return AdultEducationBusiness.class;
	}

	public AdultEducationBusiness create() throws CreateException {
		return (AdultEducationBusiness) super.createIBO();
	}
}