package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;


/**
 * @author gimmi
 */
public interface Hotel extends IDOEntity {

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#getNumberOfUnits
	 */
	public int getNumberOfUnits();

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#setNumberOfUnits
	 */
	public void setNumberOfUnits(int units);

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#getMaxPerUnit
	 */
	public int getMaxPerUnit();

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#setMaxPerUnit
	 */
	public void setMaxPerUnit(int maxPerUnit);

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#getRoomTypes
	 */
	public Collection getRoomTypes() throws IDORelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#setRoomTypeIds
	 */
	public void setRoomTypeIds(int[] roomTypeIds) throws IDORemoveRelationshipException, IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#setHotelTypeIds
	 */
	public void setHotelTypeIds(int[] hotelTypeIds) throws IDOAddRelationshipException, IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#addHotelTypeId
	 */
	public void addHotelTypeId(Object primaryKey) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#addRoomTypeId
	 */
	public void addRoomTypeId(int roomTypeId) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#addRoomTypeId
	 */
	public void addRoomTypeId(Object primaryKey) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#getHotelTypes
	 */
	public Collection getHotelTypes() throws IDORelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#setRating
	 */
	public void setRating(float rating);

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#getRating
	 */
	public float getRating();

	/**
	 * @see is.idega.idegaweb.travel.service.hotel.data.HotelBMPBean#setPrimaryKey
	 */
	public void setPrimaryKey(Object object);
}
