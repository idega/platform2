package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class HotelHomeImpl extends IDOFactory implements HotelHome {

	protected Class getEntityInterfaceClass() {
		return Hotel.class;
	}

	public Hotel create() throws javax.ejb.CreateException {
		return (Hotel) super.createIDO();
	}

	public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Hotel) super.findByPrimaryKeyIDO(pk);
	}

	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Collection postalCodes,
			Object[] supplierId, String supplierName) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((HotelBMPBean) entity).ejbFind(fromStamp, toStamp, roomTypeId, postalCodes,
				supplierId, supplierName);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] hotelTypeId,
			Collection postalCodes, Object[] supplierId, float minRating, float maxRating, String supplierName)
			throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((HotelBMPBean) entity).ejbFind(fromStamp, toStamp, roomTypeId, hotelTypeId,
				postalCodes, supplierId, minRating, maxRating, supplierName);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
