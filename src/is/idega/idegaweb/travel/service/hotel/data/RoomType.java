package is.idega.idegaweb.travel.service.hotel.data;


public interface RoomType extends com.idega.data.IDOEntity
{
 public boolean getIsValid();
 public java.lang.String getName();
 public void initializeAttributes();
 public void setIsValid(boolean p0);
 public void setName(java.lang.String p0);
}
