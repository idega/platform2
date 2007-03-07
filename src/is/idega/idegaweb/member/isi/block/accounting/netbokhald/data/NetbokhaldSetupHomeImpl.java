package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;


import com.idega.user.data.Group;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class NetbokhaldSetupHomeImpl extends IDOFactory implements
		NetbokhaldSetupHome {
	public Class getEntityInterfaceClass() {
		return NetbokhaldSetup.class;
	}

	public NetbokhaldSetup create() throws CreateException {
		return (NetbokhaldSetup) super.createIDO();
	}

	public NetbokhaldSetup findByPrimaryKey(Object pk) throws FinderException {
		return (NetbokhaldSetup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAllByClub(Group club) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((NetbokhaldSetupBMPBean) entity)
				.ejbFindAllByClub(club);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}