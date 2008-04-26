package is.idega.idegaweb.campus.block.building.data;


import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.sql.Date;
import com.idega.data.IDOEntity;
import com.idega.data.IDOFactory;

public class ApartmentTypeRentHomeImpl extends IDOFactory implements
		ApartmentTypeRentHome {
	public Class getEntityInterfaceClass() {
		return ApartmentTypeRent.class;
	}

	public ApartmentTypeRent create() throws CreateException {
		return (ApartmentTypeRent) super.createIDO();
	}

	public ApartmentTypeRent findByPrimaryKey(Object pk) throws FinderException {
		return (ApartmentTypeRent) super.findByPrimaryKeyIDO(pk);
	}

	public Collection findByType(int apartmentTypeId) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Collection ids = ((ApartmentTypeRentBMPBean) entity)
				.ejbFindByType(apartmentTypeId);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public ApartmentTypeRent findByTypeAndValidity(int aprtTypeId,
			Date dateToCheck) throws FinderException {
		IDOEntity entity = this.idoCheckOutPooledEntity();
		Object pk = ((ApartmentTypeRentBMPBean) entity)
				.ejbFindByTypeAndValidity(aprtTypeId, dateToCheck);
		this.idoCheckInPooledEntity(entity);
		return this.findByPrimaryKey(pk);
	}
}