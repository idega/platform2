package is.idega.idegaweb.travel.interfaces;

import java.sql.Timestamp;
import java.sql.SQLException;
import com.idega.data.IDOLegacyEntity;
import is.idega.idegaweb.travel.data.*;
/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public interface Booking {

  public static final int BOOKING_TYPE_ID_ONLINE_BOOKING = 1;
  public static final int BOOKING_TYPE_ID_INQUERY_BOOKING = 2;
  public static final int BOOKING_TYPE_ID_SUPPLIER_BOOKING = 3;
  public static final int BOOKING_TYPE_ID_THIRD_PARTY_BOOKING = 4;
  public static final int BOOKING_TYPE_ID_ADDITIONAL_BOOKING = 5;
  public static final int BOOKING_TYPE_ID_CORRECTION = 6;

  public static final int PAYMENT_TYPE_ID_NO_PAYMENT = 0;
  public static final int PAYMENT_TYPE_ID_CREDIT_CARD = 1;
  public static final int PAYMENT_TYPE_ID_CASH = 2;
  public static final int PAYMENT_TYPE_ID_VOUCHER = 3;
  public static final int PAYMENT_TYPE_ID_ACCOUNT = 4;

  public String getName();
  public void setName(String name);

  public Timestamp getBookingDate();
  public void setBookingDate(Timestamp timestamp);

  public Service getService();
  public int getServiceID();
  public void setServiceID(int id);

  public void setCountry(String country);
  public String getCountry();

  public String getTelephoneNumber();
  public void setTelephoneNumber(String number);

  public String getEmail();
  public void setEmail(String email);

  public String getCity();
  public void setCity(String city);

  public String getAddress();
  public void setAddress(String address);

  public int getTotalCount();
  public void setTotalCount(int totalCount);

  public int getBookingTypeID();
  public void setBookingTypeID(int id);

  public Timestamp getDateOfBooking();
  public void setDateOfBooking(Timestamp dateOfBooking);

  public String getPostalCode();
  public void setPostalCode(String code);

  public void setAttendance(int attendance);
  public int getAttendance();

  public void setPaymentTypeId(int type);
  public int getPaymentTypeId();

  public boolean getIsValid();
  public void setIsValid(boolean isValid);

  public void setReferenceNumber(String number);
  public String getReferenceNumber();

  public int getUserId();
  public void setUserId(int userId);

  public int getOwnerId();
  public void setOwnerId(int ownerId);

  public void insert() throws SQLException;
  public void update() throws SQLException;
  public void delete() throws SQLException;
  public void addTo(Class relatingEntityClass, int id) throws SQLException;
  public IDOLegacyEntity[] findRelated(IDOLegacyEntity relatingEntity) throws SQLException;

  public int getID();

  public BookingEntry[] getBookingEntries() throws SQLException;


}
