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

public class TourBooking extends GenericEntity implements Booking{

  GeneralBooking _booking;

  public TourBooking() {
    _booking = new GeneralBooking();
  }

  public TourBooking(int id) throws SQLException {
    super(id);
    _booking = new GeneralBooking(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getHotelPickupPlaceIDColumnName(),"Hotel pick-up staður",true,true,Integer.class,"many_to_one",HotelPickupPlace.class);
    addAttribute(getRoomNumberColumnName(), "Númer herbergis", true, true, String.class);
  }
  public String getEntityName() {
    return getTourBookingTableName();
  }

  public Booking getBooking() throws SQLException{
    return new GeneralBooking(this.getID());
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

  // Abstract shit

  public void insert() throws SQLException {
    super.insert();
    _booking.insert();
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

}
