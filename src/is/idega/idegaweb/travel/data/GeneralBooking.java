package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface GeneralBooking extends com.idega.data.IDOLegacyEntity,is.idega.idegaweb.travel.interfaces.Booking
{
 public java.lang.String getAddress();
 public int getAttendance();
 public java.sql.Timestamp getBookingDate();
 public is.idega.idegaweb.travel.data.BookingEntry[] getBookingEntries()throws java.sql.SQLException;
 public int getBookingTypeID();
 public java.lang.String getCity();
 public java.lang.String getComment();
 public java.lang.String getCountry();
 public java.lang.String getCreditcardAuthorizationNumber();
 public java.sql.Timestamp getDateOfBooking();
 public java.lang.String getEmail();
 public boolean getIsValid();
 public java.lang.String getName();
 public int getOwnerId();
 public int getPaymentTypeId();
 public java.lang.String getPostalCode();
 public java.lang.String getReferenceNumber();
 public is.idega.idegaweb.travel.data.Service getService();
 public int getServiceID();
 public java.lang.String getTelephoneNumber();
 public int getTotalCount();
 public int getUserId();
 public void setAddress(java.lang.String p0);
 public void setAttendance(int p0);
 public void setBookingDate(java.sql.Timestamp p0);
 public void setBookingTypeID(int p0);
 public void setCity(java.lang.String p0);
 public void setComment(java.lang.String p0);
 public void setCountry(java.lang.String p0);
 public void setCreditcardAuthorizationNumber(java.lang.String p0);
 public void setDateOfBooking(java.sql.Timestamp p0);
 public void setDefaultValues();
 public void setEmail(java.lang.String p0);
 public void setIsValid(boolean p0);
 public void setName(java.lang.String p0);
 public void setOwnerId(int p0);
 public void setPaymentTypeId(int p0);
 public void setPostalCode(java.lang.String p0);
 public void setReferenceNumber(java.lang.String p0);
 public void setServiceID(int p0);
 public void setTelephoneNumber(java.lang.String p0);
 public void setTotalCount(int p0);
 public void setUserId(int p0);
}
