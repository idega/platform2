package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface Inquery extends com.idega.data.IDOLegacyEntity
{
 public java.sql.Timestamp getAnswerDate();
 public boolean getAnswered();
 public is.idega.idegaweb.travel.data.GeneralBooking getBooking()throws java.sql.SQLException;
 public int getBookingId();
 public java.lang.String getEmail();
 public boolean getIfAnswered();
 public java.lang.String getInquery();
 public java.sql.Timestamp getInqueryDate();
 public java.sql.Timestamp getInqueryPostDate();
 public java.lang.String getName();
 public int getNumberOfSeats();
 public is.idega.idegaweb.travel.data.Service getService();
 public int getServiceID();
 public void setAnswerDate(java.sql.Timestamp p0);
 public void setAnswered(boolean p0);
 public void setBookingId(int p0);
 public void setEmail(java.lang.String p0);
 public void setIfAnswered(boolean p0);
 public void setInquery(java.lang.String p0);
 public void setInqueryDate(java.sql.Timestamp p0);
 public void setInqueryPostDate(java.sql.Timestamp p0);
 public void setName(java.lang.String p0);
 public void setNumberOfSeats(int p0);
 public void setServiceID(int p0);
}
