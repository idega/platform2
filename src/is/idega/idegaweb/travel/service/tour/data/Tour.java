package is.idega.idegaweb.travel.service.tour.data;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;


/**
 * @author gimmi
 */
public interface Tour extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getIsHotelPickup
	 */
	public boolean getIsHotelPickup() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getHotelPickup
	 */
	public boolean getHotelPickup() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setIsHotelPickup
	 */
	public void setIsHotelPickup(boolean pickup);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setHotelPickup
	 */
	public void setHotelPickup(boolean pickup);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setHotelPickupTime
	 */
	public void setHotelPickupTime(Timestamp pickupTime);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getHotelPickupTime
	 */
	public Timestamp getHotelPickupTime();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getTotalSeats
	 */
	public int getTotalSeats();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setTotalSeats
	 */
	public void setTotalSeats(int totalSeats);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getMinimumSeats
	 */
	public int getMinimumSeats();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setMinumumSeats
	 */
	public void setMinumumSeats(int seats);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setNumberOfDays
	 */
	public void setNumberOfDays(int numberOfSeats);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getNumberOfDays
	 */
	public int getNumberOfDays();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setLength
	 */
	public void setLength(float length);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getLength
	 */
	public float getLength();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setEstimatedSeatsUsed
	 */
	public void setEstimatedSeatsUsed(int seats);

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getEstimatedSeatsUsed
	 */
	public int getEstimatedSeatsUsed();

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#getTourTypes
	 */
	public Collection getTourTypes() throws IDORelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setTourTypes
	 */
	public void setTourTypes(Object[] tourTypePKs) throws IDORelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.tour.data.TourBMPBean#setPrimaryKey
	 */
	public void setPrimaryKey(Object object);
}
