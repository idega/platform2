package is.idega.travel.data;

import java.sql.*;
import com.idega.data.*;
import com.idega.core.data.*;
import com.idega.block.trade.stockroom.data.*;

/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */


public class Booking extends GenericEntity{

  public static int BOOKING_TYPE_ID_ONLINE_BOOKING = 1;
  public static int BOOKING_TYPE_ID_INQUERY_BOOKING = 2;
  public static int BOOKING_TYPE_ID_SUPPLIER_BOOKING = 3;
  public static int BOOKING_TYPE_ID_THIRD_PARTY_BOOKING = 4;
  public static int BOOKING_TYPE_ID_ADDITIONAL_BOOKING = 5;

  public Booking(){
          super();
  }
  public Booking(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class, 255);
    addAttribute(getTelephoneNumberColumnName(), "Símanúmer", true, true, String.class, 255);
    addAttribute(getEmailColumnName(), "Tölvupóstur", true, true, String.class, 255);
    addAttribute(getCityColumnName(), "Borg", true, true, String.class, 255);
    addAttribute(getAddressColumnName(), "Heimilisfang", true, true, String.class, 255);
    addAttribute(getBookingDateColumnName(), "Dagsetning", true, true, java.sql.Timestamp.class);
    addAttribute(getTotalCountColumnName(), "Fjöldi", true, true, Integer.class);
    addAttribute(getBookingTypeIDColumnName(), "Gerð bokunar", true, true, Integer.class);
    addAttribute(getServiceIDColumnName(), "Vara", true, true, Integer.class, "many-to-one", Service.class);
    addAttribute(getCountryColumnName(), "Land", true, true, String.class);
    addAttribute(getHotelPickupPlaceIDColumnName(),"Hotel pick-up staður",true,true,Integer.class,"many_to_one",HotelPickupPlace.class);
    addAttribute(getDateOfBookingColumnName(), "Hvenær bókun á sér stað", true, true, java.sql.Timestamp.class);
    addAttribute(getPostalCodeColumnName(), "Póstnúmer", true, true, String.class);
    addAttribute(getAttendanceColumnName(), "Mæting", true, true, Integer.class);
    addAttribute(getIsValidColumnName(), "valid", true, true, Boolean.class);

    this.addManyToManyRelationShip(Reseller.class);
  }

  public void setDefaultValues() {
      this.setIsValid(true);
      this.setAttendance(0);
  }



  public String getEntityName(){
    return getBookingTableName();
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public Timestamp getBookingDate() {
    return (Timestamp) getColumnValue(getBookingDateColumnName());
  }

  public void setBookingDate(Timestamp timestamp) {
    setColumn(getBookingDateColumnName(),timestamp);
  }

  public Service getService() {
    return (Service) getColumnValue(getServiceIDColumnName());
  }

  public int getServiceID() {
    return getIntColumnValue(getServiceIDColumnName());
  }

  public void setServiceID(int id) {
    setColumn(getServiceIDColumnName(), id);
  }

  public void setCountry(String country) {
    setColumn(getCountryColumnName(), country);
  }

  public String getTelephoneNumber() {
    return getStringColumnValue(getTelephoneNumberColumnName());
  }

  public void setTelephoneNumber(String number) {
    setColumn(getTelephoneNumberColumnName(), number);
  }

  public String getEmail() {
    return getStringColumnValue(getEmailColumnName());
  }

  public void setEmail(String email) {
    setColumn(getEmailColumnName(),email);
  }

  public String getCity() {
    return getStringColumnValue(getCityColumnName());
  }

  public void setCity(String city) {
    setColumn(getCityColumnName(),city);
  }

  public String getAddress() {
    return getStringColumnValue(getCityColumnName());
  }

  public void setAddress(String address) {
    setColumn(getAddressColumnName(),address);
  }

  public int getTotalCount() {
    return getIntColumnValue(getTotalCountColumnName());
  }

  public void setTotalCount(int totalCount) {
    setColumn(getTotalCountColumnName(),totalCount);
  }

  public int getBookingTypeID() {
    return getIntColumnValue(getBookingTypeIDColumnName());
  }

  public void setBookingTypeID(int id) {
    setColumn(getBookingTypeIDColumnName(),id);
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

  public Timestamp getDateOfBooking() {
    return (Timestamp) getColumnValue(getDateOfBookingColumnName());
  }

  public void setDateOfBooking(Timestamp dateOfBooking) {
    setColumn(getDateOfBookingColumnName(), dateOfBooking);
  }

  public String getPostalCode() {
    return getStringColumnValue(getPostalCodeColumnName());
  }

  public void setPostalCode(String code) {
    setColumn(getPostalCodeColumnName(), code);
  }

  public void setAttendance(int attendance) {
    setColumn(getAttendanceColumnName(), attendance);
  }

  public int getAttendance() {
    return getIntColumnValue(getAttendanceColumnName());
  }

  public boolean getIsValid() {
    return getBooleanColumnValue(getIsValidColumnName());
  }

  public void setIsValid(boolean isValid) {
    setColumn(getIsValidColumnName(), isValid);
  }

  public BookingEntry[] getBookingEntries() throws SQLException {
    return (BookingEntry[]) (BookingEntry.getStaticInstance(BookingEntry.class).findAllByColumn(BookingEntry.getBookingIDColumnName(), this.getID()));
  }

  public static String getBookingTableName(){return "TB_BOOKING";}
  public static String getNameColumnName() {return "NAME";}
  public static String getTelephoneNumberColumnName() {return "TELEPHONE_NUMBER";}
  public static String getEmailColumnName() {return "EMAIL";}
  public static String getCityColumnName() {return "CITY";}
  public static String getAddressColumnName() {return "ADDRESS";}
  public static String getBookingDateColumnName() {return "BOOKING_DATE";}
  public static String getBookingTypeIDColumnName() {return "BOOKING_TYPE_ID";}
  public static String getTotalCountColumnName() {return "TOTAL_COUNT";}
  public static String getServiceIDColumnName() {return "TB_SERVICE_ID";}
  public static String getCountryColumnName() {return "COUNTRY";}
  public static String getHotelPickupPlaceIDColumnName() {return "TB_HOTEL_PICKUP_PLACE_ID";}
  public static String getDateOfBookingColumnName() {return "DATE_OF_BOOKING";}
  public static String getPostalCodeColumnName() {return "POSTAL_CODE";}
  public static String getAttendanceColumnName() {return "ATTENDANCE";}
  public static String getIsValidColumnName() {return "IS_VALID";}
}
