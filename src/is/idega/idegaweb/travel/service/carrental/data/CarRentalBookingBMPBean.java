package is.idega.idegaweb.travel.service.carrental.data;

import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.interfaces.Booking;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;



/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class CarRentalBookingBMPBean extends com.idega.data.GenericEntity implements CarRentalBooking {

  GeneralBooking _booking;

  private static String TABLE_NAME = "TB_CAR_BOOKING";
  private static String COLUMN_PICKUP_TIME = "PICKUP_TIME";
  private static String COLUMN_DROPOFF_PLACE_ID = "DROPOFF_PLACE_ID";
  private static String COLUMN_DROPOFF_TIME = "DROPOFF_TIME";

  public CarRentalBookingBMPBean() throws RemoteException, CreateException {
    _booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).create();
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(COLUMN_PICKUP_TIME, "pickup time", true, true, Timestamp.class);
    addManyToOneRelationship(COLUMN_DROPOFF_PLACE_ID, PickupPlace.class);
    addAttribute(COLUMN_DROPOFF_TIME, "dropoff time", true, true, Timestamp.class);
  }
  
  public String getEntityName() {
    return TABLE_NAME;
  }

	public Timestamp getPickupTime() {
		return (Timestamp) this.getColumnValue(COLUMN_PICKUP_TIME);	
	}
		
	public void setPickupTime(Timestamp pickupTime) {
		setColumn(COLUMN_PICKUP_TIME, pickupTime);	
	}

	public Timestamp getDropoffTime() {
		return (Timestamp) this.getColumnValue(COLUMN_DROPOFF_TIME);	
	}
		
	public void setDropoffTime(Timestamp pickupTime) {
		setColumn(COLUMN_DROPOFF_TIME, pickupTime);	
	}
	
	public int getDropoffPlaceId() {
		return getIntColumnValue(COLUMN_DROPOFF_PLACE_ID);	
	}
	
	public PickupPlace getDropoffPlace() throws IDOLookupException, FinderException {
		return (PickupPlace) getColumnValue(COLUMN_DROPOFF_PLACE_ID);
//		int id = getDropoffPlaceId();
//		if ( id > 0 ) {
//			return ((PickupPlaceHome) IDOLookup.getHome(PickupPlace.class)).findByPrimaryKey(new Integer(id));
//		}		
//		return null;
	}
	
	public void setDropoffPlaceId(int id) {
		setColumn(COLUMN_DROPOFF_PLACE_ID, id);	
	}

  public void ejbLoad(){
    super.ejbLoad();
    try{
      GeneralBookingHome bookingHome = (GeneralBookingHome)IDOLookup.getHome(GeneralBooking.class);
      _booking = bookingHome.findByPrimaryKey(this.getPrimaryKey());
    }
    catch(Exception e){
      e.printStackTrace(System.err);
    }
  }

  public Booking getBooking() throws RemoteException, FinderException{
  	if (_booking == null) {
  		return ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(this.getPrimaryKey());
  	} else {
  		return _booking;
  	}
  }

  public PickupPlace getPickupPlace() throws RemoteException{
  	return  _booking.getPickupPlace();
  }

  public int getPickupPlaceID() throws RemoteException{
  	return _booking.getPickupPlaceID();
  }

  public void setPickupPlaceID(int id) throws RemoteException{
  	_booking.setPickupPlaceId(id);
  }

  public void setPickupExtraInfo(String roomNumber) throws RemoteException{
  	_booking.setPickupExtraInfo(roomNumber);
  }

  public String getPickupExtraInfo() throws RemoteException{
  	return _booking.getPickupExtraInfo();
  }

  public void delete() throws SQLException {
    try {
      super.remove();
      _booking.remove();
    }catch(Exception e) {
      throw new SQLException(e.getMessage());
    }
  }

  public void setIsValid(boolean isValid) throws RemoteException{
    _booking.setIsValid(isValid);
  }

  public boolean getIsValid() throws RemoteException{
    return _booking.getIsValid();
  }

  public String getName() {
    try {
      return _booking.getName();
    }catch (Exception e) {
      return "";
    }
  }
  public void setName(String name) {
    try {
      _booking.setName(name);
    }catch (Exception e) {}
  }

  public Timestamp getBookingDate()throws RemoteException {
    return _booking.getBookingDate();
  }
  public void setBookingDate(Timestamp timestamp) throws RemoteException{
    _booking.setBookingDate(timestamp);
  }

  public Service getService()throws RemoteException {
    return _booking.getService();
  }

  public int getServiceID() throws RemoteException{
    return _booking.getServiceID();
  }

  public void setServiceID(int id) throws RemoteException{
    _booking.setServiceID(id);
  }

  public void setCountry(String country)throws RemoteException {
    _booking.setCountry(country);
  }
  public String getCountry() throws RemoteException{
    return _booking.getCountry();
  }

  public String getTelephoneNumber()throws RemoteException {
    return _booking.getTelephoneNumber();
  }
  public void setTelephoneNumber(String number) throws RemoteException{
    _booking.setTelephoneNumber(number);
  }

  public String getEmail()throws RemoteException {
    return _booking.getEmail();
  }
  public void setEmail(String email) throws RemoteException{
    _booking.setEmail(email);
  }

  public String getCity() throws RemoteException{
    return _booking.getCity();
  }
  public void setCity(String city) throws RemoteException{
    _booking.setCity(city);
  }

  public String getAddress() throws RemoteException{
    return _booking.getAddress();
  }
  public void setAddress(String address)throws RemoteException {
    _booking.setAddress(address);
  }

  public int getTotalCount() throws RemoteException{
    return _booking.getTotalCount();
  }
  public void setTotalCount(int totalCount) throws RemoteException{
    _booking.setTotalCount(totalCount);
  }

  public int getBookingTypeID() throws RemoteException{
    return _booking.getBookingTypeID();
  }
  public void setBookingTypeID(int id) throws RemoteException{
    _booking.setBookingTypeID(id);
  }

  public Timestamp getDateOfBooking()throws RemoteException {
    return _booking.getDateOfBooking();
  }
  public void setDateOfBooking(Timestamp dateOfBooking)throws RemoteException{
    _booking.setDateOfBooking(dateOfBooking);
  }

  public String getPostalCode()throws RemoteException{
    return _booking.getPostalCode();
  }
  public void setPostalCode(String code) throws RemoteException{
    _booking.setPostalCode(code);
  }

  public void setAttendance(int attendance) throws RemoteException{
    _booking.setAttendance(attendance);
  }
  public int getAttendance() throws RemoteException{
    return _booking.getAttendance();
  }

  public BookingEntry[] getBookingEntries()throws RemoteException, FinderException {
    return _booking.getBookingEntries();
  }

  public void setPaymentTypeId(int type)throws RemoteException {
    _booking.setPaymentTypeId(type);
  }

  public int getPaymentTypeId()throws RemoteException {
    return _booking.getPaymentTypeId();
  }

  public void setReferenceNumber(String number) throws RemoteException{
    _booking.setReferenceNumber(number);
  }

  public String getReferenceNumber() throws RemoteException{
    return _booking.getReferenceNumber();
  }
  public int getUserId()throws RemoteException {
    return _booking.getUserId();
  }

  public void setUserId(int userId)throws RemoteException {
    _booking.setUserId(userId);
  }

  public int getOwnerId()throws RemoteException {
    return _booking.getOwnerId();
  }

  public void setOwnerId(int ownerId)throws RemoteException {
    _booking.setOwnerId(ownerId);
  }

  public String getComment()throws RemoteException {
    return _booking.getComment();
  }

  public void setComment(String comment) throws RemoteException{
    _booking.setComment(comment);
  }

  public Collection getTravelAddresses() throws RemoteException, IDORelationshipException{
    return _booking.getTravelAddresses();
  }

  public void setPrimaryKey(Object primaryKey) {
    super.setPrimaryKey(primaryKey);
  }
}
