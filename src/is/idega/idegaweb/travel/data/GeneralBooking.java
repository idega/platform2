package is.idega.idegaweb.travel.data;

import is.idega.idegaweb.travel.interfaces.Booking;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;


/**
 * @author gimmi
 */
public interface GeneralBooking extends IDOEntity, Booking {

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getName
	 */
	public String getName();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setName
	 */
	public void setName(String name);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getBookingDate
	 */
	public Timestamp getBookingDate();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setBookingDate
	 */
	public void setBookingDate(Timestamp timestamp);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getService
	 */
	public Service getService();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getServiceID
	 */
	public int getServiceID();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setServiceID
	 */
	public void setServiceID(int id);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setCountry
	 */
	public void setCountry(String country);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getTelephoneNumber
	 */
	public String getTelephoneNumber();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setTelephoneNumber
	 */
	public void setTelephoneNumber(String number);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getEmail
	 */
	public String getEmail();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setEmail
	 */
	public void setEmail(String email);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getCity
	 */
	public String getCity();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setCity
	 */
	public void setCity(String city);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getAddress
	 */
	public String getAddress();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setAddress
	 */
	public void setAddress(String address);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getTotalCount
	 */
	public int getTotalCount();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setTotalCount
	 */
	public void setTotalCount(int totalCount);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getBookingTypeID
	 */
	public int getBookingTypeID();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setBookingTypeID
	 */
	public void setBookingTypeID(int id);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getDateOfBooking
	 */
	public Timestamp getDateOfBooking();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setDateOfBooking
	 */
	public void setDateOfBooking(Timestamp dateOfBooking);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getPostalCode
	 */
	public String getPostalCode();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setPostalCode
	 */
	public void setPostalCode(String code);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getCountry
	 */
	public String getCountry();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setAttendance
	 */
	public void setAttendance(int attendance);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getAttendance
	 */
	public int getAttendance();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setPaymentTypeId
	 */
	public void setPaymentTypeId(int id);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getPaymentTypeId
	 */
	public int getPaymentTypeId();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getIsValid
	 */
	public boolean getIsValid();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setIsValid
	 */
	public void setIsValid(boolean isValid);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getCode
	 */
	public String getCode();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setCode
	 */
	public void setCode(String code);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getBookingEntries
	 */
	public BookingEntry[] getBookingEntries() throws FinderException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setReferenceNumber
	 */
	public void setReferenceNumber(String number);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getReferenceNumber
	 */
	public String getReferenceNumber();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getUserId
	 */
	public int getUserId();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setUserId
	 */
	public void setUserId(int userId);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getOwnerId
	 */
	public int getOwnerId();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setOwnerId
	 */
	public void setOwnerId(int ownerId);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getComment
	 */
	public String getComment();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setComment
	 */
	public void setComment(String comment);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getCreditcardAuthorizationNumber
	 */
	public String getCreditcardAuthorizationNumber();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setCreditcardAuthorizationNumber
	 */
	public void setCreditcardAuthorizationNumber(String number);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getPickupPlaceID
	 */
	public int getPickupPlaceID();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getPickupPlace
	 */
	public PickupPlace getPickupPlace();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setPickupPlace
	 */
	public void setPickupPlace(PickupPlace pPlace) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setPickupPlaceId
	 */
	public void setPickupPlaceId(int pickupPlaceId);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getPickupExtraInfo
	 */
	public String getPickupExtraInfo();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setPickupExtraInfo
	 */
	public void setPickupExtraInfo(String info);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getRefererUrl
	 */
	public String getRefererUrl();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setRefererUrl
	 */
	public void setRefererUrl(String url);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#store
	 */
	public void store();

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#removeAllTravelAddresses
	 */
	public void removeAllTravelAddresses() throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#addTravelAddress
	 */
	public void addTravelAddress(TravelAddress tAddress) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getTravelAddresses
	 */
	public Collection getTravelAddresses() throws IDORelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#setPrimaryKey
	 */
	public void setPrimaryKey(Object primaryKey);

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#removeFromReseller
	 */
	public void removeFromReseller(Reseller reseller) throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#removeFromAllResellers
	 */
	public void removeFromAllResellers() throws IDORemoveRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#addToReseller
	 */
	public void addToReseller(Reseller reseller) throws IDOAddRelationshipException;

	/**
	 * @see is.idega.idegaweb.travel.data.GeneralBookingBMPBean#getReseller
	 */
	public Reseller getReseller() throws RemoteException, IDORelationshipException, FinderException;
}
