package is.idega.idegaweb.travel.service.tour.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOFactory;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public class TourHomeImpl extends IDOFactory implements TourHome {

	protected Class getEntityInterfaceClass() {
		return Tour.class;
	}

	public Tour create() throws javax.ejb.CreateException {
		return (Tour) super.createIDO();
	}

	public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException {
		return (Tour) super.findByPrimaryKeyIDO(pk);
	}

	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] tourTypeId, Collection postalCodes,
			Object[] supplierId, String supplierName) throws FinderException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((TourBMPBean) entity).ejbFind(fromStamp, toStamp, tourTypeId, postalCodes,
				supplierId, supplierName);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}
}
