package is.idega.idegaweb.campus.block.application.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class ApplicationSessionHomeImpl extends IBOHomeImpl implements ApplicationSessionHome {
	public Class getBeanInterfaceClass() {
		return ApplicationSession.class;
	}

	public ApplicationSession create() throws CreateException {
		return (ApplicationSession) super.createIBO();
	}
}