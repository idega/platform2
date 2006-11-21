package is.idega.block.nationalregister.data;


import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;

public class NationalRegisterDeceasedHomeImpl extends IDOFactory implements NationalRegisterDeceasedHome {

	public Class getEntityInterfaceClass() {
		return NationalRegisterDeceased.class;
	}

	public NationalRegisterDeceased create() throws CreateException {
		return (NationalRegisterDeceased) super.createIDO();
	}

	public NationalRegisterDeceased findByPrimaryKey(Object pk) throws FinderException {
		return (NationalRegisterDeceased) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((NationalRegisterDeceasedBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findAllBySSN(String ssn) throws FinderException, RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((NationalRegisterDeceasedBMPBean) entity).ejbFindAllBySSN(ssn);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}