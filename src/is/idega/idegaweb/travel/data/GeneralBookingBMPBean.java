package is.idega.idegaweb.travel.data;

import is.idega.idegaweb.travel.interfaces.Booking;
import java.sql.*;
import com.idega.util.CypherText;
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


public class GeneralBookingBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.GeneralBooking,is.idega.idegaweb.travel.interfaces.Booking {

  public GeneralBookingBMPBean(){
          super();
  }

  public GeneralBookingBMPBean(int id)throws SQLException{
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
    addAttribute(getDateOfBookingColumnName(), "Hvenær bókun á sér stað", true, true, java.sql.Timestamp.class);
    addAttribute(getPostalCodeColumnName(), "Póstnúmer", true, true, String.class);
    addAttribute(getAttendanceColumnName(), "Mæting", true, true, Integer.class);
    addAttribute(getPaymentTypeIdColumnName(), "Gerð greiðslu", true, true, Integer.class);
    addAttribute(getIsValidColumnName(), "valid", true, true, Boolean.class);
    addAttribute(getReferenceNumberColumnName(), "reference number", true, true, String.class);
    addAttribute(getOwnerIdColumnName(), "owner id", true, true, Integer.class);
    addAttribute(getUserIdColumnName(), "user id", true, true, Integer.class);
    addAttribute(getCommentColumnName(), "comment", true, true, String.class);
    addAttribute(getCreditcardAuthorizationNumberColumnName(), "cc auth", true, true, String.class);

    this.addManyToManyRelationShip(Reseller.class);
    this.addManyToManyRelationShip(Address.class);
    this.addManyToManyRelationShip(TravelAddress.class);
  }


  public void setDefaultValues() {
      this.setIsValid(true);
      this.setAttendance(-1000);
      this.setPaymentTypeId(Booking.PAYMENT_TYPE_ID_NO_PAYMENT);
      //this.setDiscountTypeId(Booking.DISCOUNT_TYPE_ID_PERCENT);
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
    return getStringColumnValue(getAddressColumnName());
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

  public String getCountry() {
    return getStringColumnValue(getCountryColumnName());
  }

  public void setAttendance(int attendance) {
    setColumn(getAttendanceColumnName(), attendance);
  }

  public int getAttendance() {
    return getIntColumnValue(getAttendanceColumnName());
  }

  public void setPaymentTypeId(int id) {
    setColumn(getPaymentTypeIdColumnName(), id);
  }

  public int getPaymentTypeId() {
    return getIntColumnValue(getPaymentTypeIdColumnName());
  }

  public boolean getIsValid() {
    return getBooleanColumnValue(getIsValidColumnName());
  }

  public void setIsValid(boolean isValid) {
    setColumn(getIsValidColumnName(), isValid);
  }

  public BookingEntry[] getBookingEntries() throws SQLException {
    return (BookingEntry[]) (is.idega.idegaweb.travel.data.BookingEntryBMPBean.getStaticInstance(BookingEntry.class).findAllByColumn(is.idega.idegaweb.travel.data.BookingEntryBMPBean.getBookingIDColumnName(), this.getID()));
  }

  public void setReferenceNumber(String number) {
    setColumn(getReferenceNumberColumnName(), number);
  }

  public String getReferenceNumber() {
    return getStringColumnValue(getReferenceNumberColumnName());
  }

  public int getUserId() {
    return getIntColumnValue(getUserIdColumnName());
  }

  public void setUserId(int userId) {
    setColumn(getUserIdColumnName(), userId);
  }

  public int getOwnerId() {
    return getIntColumnValue(getOwnerIdColumnName());
  }

  public void setOwnerId(int ownerId) {
    setColumn(getOwnerIdColumnName(), ownerId);
  }

  public String getComment() {
    return getStringColumnValue(getCommentColumnName());
  }

  public void setComment(String comment) {
    setColumn(getCommentColumnName(), comment);
  }

  public String getCreditcardAuthorizationNumber() {
    return getStringColumnValue(getCreditcardAuthorizationNumberColumnName());
  }

  public void setCreditcardAuthorizationNumber(String number) {
    setColumn(getCreditcardAuthorizationNumberColumnName(), number);
  }


  public void insert() throws SQLException {
    CypherText cyph = new CypherText();
    String key = cyph.getKey(8);
    GeneralBooking[] bookings = (GeneralBooking[]) findAllByColumn(getReferenceNumberColumnName(), key);
    while (bookings.length > 0) {
      key = cyph.getKey(8);
      bookings = (GeneralBooking[]) findAllByColumn(getReferenceNumberColumnName(), key);
    }
    setReferenceNumber(key);
    super.insert();
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
  public static String getDateOfBookingColumnName() {return "DATE_OF_BOOKING";}
  public static String getPostalCodeColumnName() {return "POSTAL_CODE";}
  public static String getAttendanceColumnName() {return "ATTENDANCE";}
  public static String getPaymentTypeIdColumnName() {return "PAYMENT_TYPE";}
  public static String getIsValidColumnName() {return "IS_VALID";}
  public static String getReferenceNumberColumnName() {return "REFERENCE_NUMBER";}
  public static String getOwnerIdColumnName() {return "OWNER_ID";}
  public static String getUserIdColumnName() {return "IC_USER_ID";}
  public static String getCommentColumnName() {return "BK_COMMENT";}
  public static String getCreditcardAuthorizationNumberColumnName() {return "CC_AUTH_NUMBER";}


}

