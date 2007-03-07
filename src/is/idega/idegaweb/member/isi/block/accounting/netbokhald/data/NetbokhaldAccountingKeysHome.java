package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;


import java.util.Collection;
import javax.ejb.CreateException;
import com.idega.data.IDOHome;
import javax.ejb.FinderException;

public interface NetbokhaldAccountingKeysHome extends IDOHome {
	public NetbokhaldAccountingKeys create() throws CreateException;

	public NetbokhaldAccountingKeys findByPrimaryKey(Object pk)
			throws FinderException;

	public Collection findAllBySetupID(NetbokhaldSetup setup)
			throws FinderException;

	public NetbokhaldAccountingKeys findBySetupIDTypeAndKey(
			NetbokhaldSetup setup, String type, int key) throws FinderException;
}