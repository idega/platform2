package is.idega.idegaweb.travel.interfaces;

import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.Service;

import java.rmi.RemoteException;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.data.IDOEntity;
/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public interface Booking extends IDOEntity{

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

  public String getName() throws RemoteException;
  public void setName(String name)throws RemoteException;

  public Timestamp getBookingDate()throws RemoteException;
  public void setBookingDate(Timestamp timestamp)throws RemoteException;

  public Service getService()throws RemoteException;
  public int getServiceID()throws RemoteException;
  public void setServiceID(int id)throws RemoteException;

  public void setCountry(String country)throws RemoteException;
  public String getCountry()throws RemoteException;

  public String getTelephoneNumber()throws RemoteException;
  public void setTelephoneNumber(String number)throws RemoteException;

  public String getEmail()throws RemoteException;
  public void setEmail(String email)throws RemoteException;

  public String getCity()throws RemoteException;
  public void setCity(String city)throws RemoteException;

  public String getAddress()throws RemoteException;
  public void setAddress(String address)throws RemoteException;

  public int getTotalCount()throws RemoteException;
  public void setTotalCount(int totalCount)throws RemoteException;

  public int getBookingTypeID()throws RemoteException;
  public void setBookingTypeID(int id)throws RemoteException;

  public Timestamp getDateOfBooking()throws RemoteException;
  public void setDateOfBooking(Timestamp dateOfBooking)throws RemoteException;

  public String getPostalCode()throws RemoteException;
  public void setPostalCode(String code)throws RemoteException;

  public void setAttendance(int attendance)throws RemoteException;
  public int getAttendance()throws RemoteException;

  public void setPaymentTypeId(int type)throws RemoteException;
  public int getPaymentTypeId()throws RemoteException;

  public boolean getIsValid()throws RemoteException;
  public void setIsValid(boolean isValid)throws RemoteException;

  public void setReferenceNumber(String number)throws RemoteException;
  public String getReferenceNumber()throws RemoteException;

  public int getUserId()throws RemoteException;
  public void setUserId(int userId)throws RemoteException;

  public int getOwnerId()throws RemoteException;
  public void setOwnerId(int ownerId)throws RemoteException;

  public String getComment()throws RemoteException;
  public void setComment(String comment)throws RemoteException;
  
  public int getPickupPlaceID() throws RemoteException;
  public PickupPlace getPickupPlace() throws RemoteException;
  public String getPickupExtraInfo() throws RemoteException;
  
 public java.util.Collection getTravelAddresses()throws com.idega.data.IDORelationshipException, java.rmi.RemoteException;

  /*
  public void insert() throws SQLException, RemoteException;
  public void update() throws SQLException, RemoteException;
  public void delete() throws SQLException, RemoteException;
  public void addTo(Class relatingEntityClass, int id) throws SQLException, RemoteException;
  public IDOLegacyEntity[] findRelated(IDOLegacyEntity relatingEntity) throws SQLException, RemoteException;
*/
  public int getID() throws RemoteException;

  public BookingEntry[] getBookingEntries() throws FinderException, RemoteException;


}
