package is.idega.idegaweb.travel.service.tour.data;

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

  public TourBookingBMPBean() {
    _booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).createLegacy();
  }

  public TourBookingBMPBean(int id) throws SQLException {
    /*
    super(id);
    _booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKeyLegacy(id);
    System.err.println("==========================================");
    System.err.println("id = "+id);
    if (_booking == null) {
      System.err.println("_booking == null");
    }else {
      System.err.println("_bookingId = "+_booking.getID());
    }
    System.err.println("==========================================");
    */
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getHotelPickupPlaceIDColumnName(),"Hotel pick-up staður",true,true,Integer.class,"many_to_one",HotelPickupPlace.class);
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

  public Booking getBooking() throws SQLException{
    return ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKeyLegacy(this.getID());
  }

  public HotelPickupPlace getHotelPickupPlace() {
    return (HotelPickupPlace) getColumnValue(getHotelPickupPlaceIDColumnName());
  }

  public int getHotelPickupPlaceID() {
    return getIntColumnValue(getHotelPickupPlaceIDColumnName());
  }

  public void setHotelPickupPlaceID(int id) {
    setColumn(getHotelPickupPlaceIDColumnName(),id);
  }

  public void setRoomNumber(String roomNumber) {
    setColumn(getRoomNumberColumnName(), roomNumber);
  }

  public String getRoomNumber() {
    return getStringColumnValue(getRoomNumberColumnName());
  }

  public static String getTourBookingTableName(){return "TB_BOOKING_TOUR";}
  public static String getHotelPickupPlaceIDColumnName() {return "TB_HOTEL_PICKUP_PLACE_ID";}
  public static String getRoomNumberColumnName() {return "ROOM_NUMBER";}


  public void insert() throws SQLException {
    super.insert();
    _booking.setColumn(this.getIDColumnName(), super.getID());
    try {
      _booking.update();
    }catch (SQLException sql) {
      debug("UPDATE FAILED ... Inserting......");
      //sql.printStackTrace(System.err);
      _booking.insert();
    }
  }

  public void update() throws SQLException {
    super.update();
    _booking.update();
  }

  public void delete() throws SQLException {
    super.delete();
    _booking.delete();
  }

  public void setIsValid(boolean isValid) {
    _booking.setIsValid(isValid);
  }

  public boolean getIsValid() {
    return _booking.getIsValid();
  }

  public String getName() {
    return _booking.getName();
  }
  public void setName(String name) {
    _booking.setName(name);
  }

  public Timestamp getBookingDate() {
    return _booking.getBookingDate();
  }
  public void setBookingDate(Timestamp timestamp) {
    _booking.setBookingDate(timestamp);
  }

  public Service getService() {
    return _booking.getService();
  }

  public int getServiceID() {
    return _booking.getServiceID();
  }

  public void setServiceID(int id) {
    _booking.setServiceID(id);
  }

  public void setCountry(String country) {
    _booking.setCountry(country);
  }
  public String getCountry() {
    return _booking.getCountry();
  }

  public String getTelephoneNumber() {
    return _booking.getTelephoneNumber();
  }
  public void setTelephoneNumber(String number) {
    _booking.setTelephoneNumber(number);
  }

  public String getEmail() {
    return _booking.getEmail();
  }
  public void setEmail(String email) {
    _booking.setEmail(email);
  }

  public String getCity() {
    return _booking.getCity();
  }
  public void setCity(String city) {
    _booking.setCity(city);
  }

  public String getAddress() {
    return _booking.getAddress();
  }
  public void setAddress(String address) {
    _booking.setAddress(address);
  }

  public int getTotalCount() {
    return _booking.getTotalCount();
  }
  public void setTotalCount(int totalCount) {
    _booking.setTotalCount(totalCount);
  }

  public int getBookingTypeID() {
    return _booking.getBookingTypeID();
  }
  public void setBookingTypeID(int id) {
    _booking.setBookingTypeID(id);
  }

  public Timestamp getDateOfBooking() {
    return _booking.getDateOfBooking();
  }
  public void setDateOfBooking(Timestamp dateOfBooking){
    _booking.setDateOfBooking(dateOfBooking);
  }

  public String getPostalCode(){
    return _booking.getPostalCode();
  }
  public void setPostalCode(String code) {
    _booking.setPostalCode(code);
  }

  public void setAttendance(int attendance) {
    _booking.setAttendance(attendance);
  }
  public int getAttendance() {
    return _booking.getAttendance();
  }

  public BookingEntry[] getBookingEntries() throws SQLException {
    return _booking.getBookingEntries();
  }

  public void setPaymentTypeId(int type) {
    _booking.setPaymentTypeId(type);
  }

  public int getPaymentTypeId() {
    return _booking.getPaymentTypeId();
  }

  public void setReferenceNumber(String number) {
    _booking.setReferenceNumber(number);
  }

  public String getReferenceNumber() {
    return _booking.getReferenceNumber();
  }
  public int getUserId() {
    return _booking.getUserId();
  }

  public void setUserId(int userId) {
    _booking.setUserId(userId);
  }

  public int getOwnerId() {
    return _booking.getOwnerId();
  }

  public void setOwnerId(int ownerId) {
    _booking.setOwnerId(ownerId);
  }

  public String getComment() {
    return _booking.getComment();
  }

  public void setComment(String comment) {
    _booking.setComment(comment);
  }


}
