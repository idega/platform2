package is.idega.idegaweb.travel.service.tour.data;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import java.util.Collection;
import com.idega.data.*;
import is.idega.idegaweb.travel.data.*;
import java.sql.*;
import is.idega.idegaweb.travel.interfaces.Booking;



/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TourBookingBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.service.tour.data.TourBooking,is.idega.idegaweb.travel.interfaces.Booking {

  GeneralBooking _booking;

  public TourBookingBMPBean() throws RemoteException, CreateException {
    _booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).create();
  }


  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getHotelPickupPlaceIDColumnName(),"Hotel pick-up staður",true,true,Integer.class,"many_to_one",PickupPlace.class);
    addAttribute(getRoomNumberColumnName(), "Númer herbergis", true, true, String.class);
  }
  public String getEntityName() {
    return getTourBookingTableName();
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
    return ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(this.getPrimaryKey());
  }

  public PickupPlace getPickupPlace() throws RemoteException{
  	PickupPlace place = _booking.getPickupPlace();
  	if (place != null) {
  		return place;	
  	}else {
	    return (PickupPlace) getColumnValue(getHotelPickupPlaceIDColumnName());
  	}
  }

  public int getPickupPlaceID() throws RemoteException{
  	int id = _booking.getPickupPlaceID();
  	if (id > 0) {
  		return id;
  	}else {
  		id = getIntColumnValue(getHotelPickupPlaceIDColumnName());
  		if (id > 0) {
  			System.out.println("[TourBookingBMPBean] setting GeneralBooking Pickup Id");
	  		_booking.setPickupPlaceId(id);
	  		_booking.store();
  		}
	    return id;
  	}
  }

  public void setPickupPlaceID(int id) throws RemoteException{
  	_booking.setPickupPlaceId(id);
//    setColumn(getHotelPickupPlaceIDColumnName(),id);
  }

  public void setPickupExtraInfo(String roomNumber) throws RemoteException{
  	_booking.setPickupExtraInfo(roomNumber);
    //setColumn(getRoomNumberColumnName(), roomNumber);
  }

  public String getPickupExtraInfo() throws RemoteException{
  	String info = _booking.getPickupExtraInfo();
  	if (info != null) {
  		return info;	
  	}else {
  		info = getStringColumnValue(getRoomNumberColumnName());
  		if (info != null) {
  			System.out.println("[TourBookingBMPBean] setting GeneralBooking Pickup Info");
	  		_booking.setPickupExtraInfo(info);
	  		_booking.store();
  		}
	    return info;
  	}
  }

  public static String getTourBookingTableName(){return "TB_BOOKING_TOUR";}
  public static String getHotelPickupPlaceIDColumnName() {return "TB_HOTEL_PICKUP_PLACE_ID";}
  public static String getRoomNumberColumnName() {return "ROOM_NUMBER";}
  public static String getTourBookingIDColumnName() {return getTourBookingTableName()+"_ID";}

  public void store()  {
//    try {
      super.store();
//      _booking.setPrimaryKey(this.getPrimaryKey());
  //      _booking.setColumn(this.getIDColumnName(), super.getID());
//      _booking.store();
//    }catch(RemoteException e) {
//      throw new IDOStoreException(e.getMessage());
//    }
  }
/*
  public void update() throws SQLException {
    try {
      super.store();
      _booking.store();
    }catch(Exception e) {
      throw new SQLException(e.getMessage());
    }
  }
*/
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
