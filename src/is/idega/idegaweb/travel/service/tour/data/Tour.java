package is.idega.idegaweb.travel.service.tour.data;

import javax.ejb.*;

public interface Tour extends com.idega.data.IDOEntity
{
 public int getEstimatedSeatsUsed() throws java.rmi.RemoteException;
 public boolean getHotelPickup()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.sql.Timestamp getHotelPickupTime() throws java.rmi.RemoteException;
 public boolean getIsHotelPickup()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public float getLength() throws java.rmi.RemoteException;
 public int getMinimumSeats() throws java.rmi.RemoteException;
 public int getNumberOfDays() throws java.rmi.RemoteException;
 public int getTotalSeats() throws java.rmi.RemoteException;
 public void setDefaultValues() throws java.rmi.RemoteException;
 public void setEstimatedSeatsUsed(int p0) throws java.rmi.RemoteException;
 public void setHotelPickup(boolean p0) throws java.rmi.RemoteException;
 public void setHotelPickupTime(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setIsHotelPickup(boolean p0) throws java.rmi.RemoteException;
 public void setLength(float p0) throws java.rmi.RemoteException;
 public void setMinumumSeats(int p0) throws java.rmi.RemoteException;
 public void setNumberOfDays(int p0) throws java.rmi.RemoteException;
 public void setPrimaryKey(java.lang.Object p0) throws java.rmi.RemoteException;
 public void setTotalSeats(int p0) throws java.rmi.RemoteException;
}
