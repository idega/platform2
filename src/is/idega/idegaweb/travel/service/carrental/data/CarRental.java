package is.idega.idegaweb.travel.service.carrental.data;

import is.idega.idegaweb.travel.data.PickupPlace;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;


/**
 * @author gimmi
 */
public interface CarRental extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#getPickupPlaces
	 */
	public Collection getPickupPlaces() throws IDOLookupException, IDORelationshipException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#getDropoffPlaces
	 */
	public Collection getDropoffPlaces() throws IDOLookupException, IDORelationshipException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#addPickupPlace
	 */
	public void addPickupPlace(PickupPlace pPlace) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#addDropoffPlace
	 */
	public void addDropoffPlace(PickupPlace pPlace) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#removeAllPickupPlaces
	 */
	public void removeAllPickupPlaces() throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#removeAllDropoffPlaces
	 */
	public void removeAllDropoffPlaces() throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.carrental.data.CarRentalBMPBean#setPrimaryKey
	 */
	public void setPrimaryKey(Object obj);
}
