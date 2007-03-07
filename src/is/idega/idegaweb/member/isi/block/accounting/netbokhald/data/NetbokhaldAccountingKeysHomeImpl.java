package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class NetbokhaldAccountingKeysHomeImpl extends IDOFactory implements
		NetbokhaldAccountingKeysHome {
	public Class getEntityInterfaceClass() {
		return NetbokhaldAccountingKeys.class;
	}

	public NetbokhaldAccountingKeys create() throws CreateException {
		return (NetbokhaldAccountingKeys) super.createIDO();
	}

	public NetbokhaldAccountingKeys findByPrimaryKey(Object pk)
			throws FinderException {
		return (NetbokhaldAccountingKeys) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllBySetupID(NetbokhaldSetup setup)
			throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((NetbokhaldAccountingKeysBMPBean) entity)
				.ejbFindAllBySetupID(setup);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public NetbokhaldAccountingKeys findBySetupIDTypeAndKey(
			NetbokhaldSetup setup, String type, int key) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((NetbokhaldAccountingKeysBMPBean) entity)
				.ejbFindBySetupIDTypeAndKey(setup, type, key);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}