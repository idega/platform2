package is.idega.idegaweb.travel.service.hotel.data;


public interface Hotel extends com.idega.data.IDOEntity
{
 public int getMaxPerUnit();
 public int getNumberOfUnits();
 public int getRoomTypeId();
 public void initializeAttributes();
 public void setMaxPerUnit(int p0);
 public void setNumberOfUnits(int p0);
 public void setPrimaryKey(java.lang.Object p0);
 public void setRoomTypeId(int p0);
}
