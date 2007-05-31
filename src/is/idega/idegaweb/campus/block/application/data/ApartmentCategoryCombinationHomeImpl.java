package is.idega.idegaweb.campus.block.application.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ApartmentCategoryCombinationHomeImpl extends IDOFactory implements
		ApartmentCategoryCombinationHome {
	public Class getEntityInterfaceClass() {
		return ApartmentCategoryCombination.class;
	}

	public ApartmentCategoryCombination create() throws CreateException {
		return (ApartmentCategoryCombination) super.createIDO();
	}

	public ApartmentCategoryCombination findByPrimaryKey(Object pk)
			throws FinderException {
		return (ApartmentCategoryCombination) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findAll() throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentCategoryCombinationBMPBean) entity)
				.ejbFindAll();
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}