package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface HotelHome extends IDOHome {

	public Hotel create() throws javax.ejb.CreateException;

	public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#ejbFind
	 */
	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Collection postalCodes,
			Object[] supplierId, String supplierName) throws FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#ejbFind
	 */
	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] hotelTypeId,
			Collection postalCodes, Object[] supplierId, float minRating, float maxRating, String supplierName)
			throws FinderException;
}
