package is.idega.idegaweb.travel.service.tour.data;


public interface Tour extends com.idega.data.IDOEntity
{
 public int getEstimatedSeatsUsed();
 public boolean getHotelPickup()throws java.rmi.RemoteException;
 public java.sql.Timestamp getHotelPickupTime();
 public boolean getIsHotelPickup()throws java.rmi.RemoteException;
 public float getLength();
 public int getMinimumSeats();
 public int getNumberOfDays();
 public int getTotalSeats();
 public java.util.Collection getTourTypes()throws com.idega.data.IDORelationshipException;
 public void setEstimatedSeatsUsed(int p0);
 public void setHotelPickup(boolean p0);
 public void setHotelPickupTime(java.sql.Timestamp p0);
 public void setIsHotelPickup(boolean p0);
 public void setLength(float p0);
 public void setMinumumSeats(int p0);
 public void setNumberOfDays(int p0);
 public void setPrimaryKey(java.lang.Object p0);
 public void setTotalSeats(int p0);
 public void setTourTypes(java.lang.Object[] p0)throws com.idega.data.IDORelationshipException;
}
