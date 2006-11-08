package is.idega.idegaweb.member.isi.block.accounting.webservice.netbokhald.business;


import javax.ejb.CreateException;
import com.idega.business.IBOHomeImpl;

public class NetbokhaldBusinessHomeImpl extends IBOHomeImpl implements NetbokhaldBusinessHome {
	public Class getBeanInterfaceClass() {
		return NetbokhaldBusiness.class;
	}

	public NetbokhaldBusiness create() throws CreateException {
		return (NetbokhaldBusiness) super.createIBO();
	}
}