package is.idega.idegaweb.travel.service.tour.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface TourHome extends IDOHome {

	public Tour create() throws javax.ejb.CreateException;

	public Tour findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#ejbFind
	 */
	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] tourTypeId, Collection postalCodes,
			Object[] supplierId, String supplierName) throws FinderException;
}
