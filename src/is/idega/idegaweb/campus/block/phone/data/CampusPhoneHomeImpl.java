package is.idega.idegaweb.campus.block.phone.data;


import com.idega.data.IDOFactory;
import javax.ejb.CreateException;
import com.idega.data.IDOEntity;
import javax.ejb.FinderException;
import java.util.Collection;

public class CampusPhoneHomeImpl extends IDOFactory implements CampusPhoneHome {
	public Class getEntityInterfaceClass() {
		return CampusPhone.class;
	}

	public CampusPhone create() throws CreateException {
		return (CampusPhone) super.createIDO();
	}

	public CampusPhone findByPrimaryKey(Object pk) throws FinderException {
		return (CampusPhone) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusPhoneBMPBean) entity).ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection findByPhoneNumber(String number) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((CampusPhoneBMPBean) entity)
				.ejbFindByPhoneNumber(number);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}