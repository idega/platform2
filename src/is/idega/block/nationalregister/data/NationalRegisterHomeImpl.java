package is.idega.block.nationalregister.data;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;


/**
 * @author Joakim
 *
 */
public class NationalRegisterHomeImpl extends IDOFactory implements NationalRegisterHome {

	protected Class getEntityInterfaceClass() {
		return NationalRegister.class;
	}

	public NationalRegister create() throws javax.ejb.CreateException {
		return (NationalRegister) super.createIDO();
	}

	public NationalRegister findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (NationalRegister) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((NationalRegisterBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySSN(String ssn) throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((NationalRegisterBMPBean) entity).ejbFindAllBySSN(ssn);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
