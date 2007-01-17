package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;


import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class NetbokhaldSetupHomeImpl extends IDOFactory implements NetbokhaldSetupHome {
	public Class getEntityInterfaceClass() {
		return NetbokhaldSetup.class;
	}

	public NetbokhaldSetup create() throws CreateException {
		return (NetbokhaldSetup) super.createIDO();
	}

	public NetbokhaldSetup findByPrimaryKey(Object pk) throws FinderException {
		return (NetbokhaldSetup) super.findByPrimaryKeyIDO(pk);
	}
}