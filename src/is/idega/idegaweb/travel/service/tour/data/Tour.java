package is.idega.idegaweb.travel.service.tour.data;

import javax.ejb.*;

public interface Tour extends com.idega.data.IDOLegacyEntity
{
 public int getEstimatedSeatsUsed();
 public boolean getHotelPickup();
 public java.sql.Timestamp getHotelPickupTime();
 public boolean getIsHotelPickup();
 public float getLength();
 public int getMinimumSeats();
 public int getNumberOfDays();
 public int getTotalSeats();
 public void setDefaultValues();
 public void setEstimatedSeatsUsed(int p0);
 public void setHotelPickup(boolean p0);
 public void setHotelPickupTime(java.sql.Timestamp p0);
 public void setIsHotelPickup(boolean p0);
 public void setLength(float p0);
 public void setMinumumSeats(int p0);
 public void setNumberOfDays(int p0);
 public void setTotalSeats(int p0);
}
