package is.idega.idegaweb.travel.service.carrental.data;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOHome;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface CarRentalHome extends IDOHome {

	public CarRental create() throws javax.ejb.CreateException;

	public CarRental findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#ejbFind
	 */
	public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Collection postalCodes, Object[] supplierId,
			String supplierName) throws FinderException;
}
