package is.idega.idegaweb.travel.service.tour.data;


public interface TourBooking extends com.idega.data.IDOEntity,is.idega.idegaweb.travel.interfaces.Booking
{
 public void delete()throws java.sql.SQLException, java.rmi.RemoteException;
 public java.lang.String getAddress()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getAttendance()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.interfaces.Booking getBooking()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.sql.Timestamp getBookingDate()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.BookingEntry[] getBookingEntries()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getBookingTypeID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getCity()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getComment()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getCountry()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.sql.Timestamp getDateOfBooking()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getEmail()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.PickupPlace getPickupPlace() throws java.rmi.RemoteException;
 public int getPickupPlaceID() throws java.rmi.RemoteException;
 public boolean getIsValid()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getName() throws java.rmi.RemoteException;
 public int getOwnerId()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getPaymentTypeId()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getPostalCode()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getReferenceNumber()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getPickupExtraInfo() throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.Service getService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getServiceID()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getTelephoneNumber()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public int getTotalCount()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection getTravelAddresses()throws com.idega.data.IDORelationshipException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getUserId()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAddress(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setAttendance(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setBookingDate(java.sql.Timestamp p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setBookingTypeID(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setCity(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setComment(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setCountry(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setDateOfBooking(java.sql.Timestamp p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setEmail(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setPickupPlaceID(int p0) throws java.rmi.RemoteException;
 public void setIsValid(boolean p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setName(java.lang.String p0) throws java.rmi.RemoteException;
 public void setOwnerId(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setPaymentTypeId(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setPostalCode(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setPrimaryKey(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setReferenceNumber(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setPickupExtraInfo(java.lang.String p0) throws java.rmi.RemoteException;
 public void setServiceID(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setTelephoneNumber(java.lang.String p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setTotalCount(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setUserId(int p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
}
