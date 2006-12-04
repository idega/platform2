package is.idega.idegaweb.campus.nortek.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class NortekSetupHomeImpl extends IDOFactory implements NortekSetupHome {
	public Class getEntityInterfaceClass() {
		return NortekSetup.class;
	}

	public NortekSetup create() throws CreateException {
		return (NortekSetup) super.createIDO();
	}

	public NortekSetup findByPrimaryKey(Object pk) throws FinderException {
		return (NortekSetup) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((NortekSetupBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public NortekSetup findEntry() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((NortekSetupBMPBean) entity).ejbFindEntry();
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}