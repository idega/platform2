package is.idega.idegaweb.travel.service.carrental.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class CarRentalHomeImpl extends IDOFactory implements CarRentalHome {

	protected Class getEntityInterfaceClass() {
		return CarRental.class;
	}

	public CarRental create() throws javax.ejb.CreateException {
		return (CarRental) super.createIDO();
	}

	public CarRental findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (CarRental) super.findByPrimaryKeyIDO(pk);
	}

	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Collection postalCodes, Object[] supplierId,
			String supplierName) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((CarRentalBMPBean) entity).ejbFind(fromStamp, toStamp, postalCodes, supplierId,
				supplierName);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
