package is.idega.idegaweb.travel.service.hotel.data;


public interface Hotel extends com.idega.data.IDOEntity
{
 public void addHotelTypeId(java.lang.Object p0)throws com.idega.data.IDOAddRelationshipException;
 public void addRoomTypeId(java.lang.Object p0)throws com.idega.data.IDOAddRelationshipException;
 public void addRoomTypeId(int p0)throws com.idega.data.IDOAddRelationshipException;
 public java.util.Collection getHotelTypes()throws com.idega.data.IDORelationshipException;
 public int getMaxPerUnit();
 /**
  * @deprecated
  */
 public int getRoomTypeId();
 public int getNumberOfUnits();
 public float getRating();
 public java.util.Collection getRoomTypes()throws com.idega.data.IDORelationshipException;
 public void setHotelTypeIds(int[] p0)throws com.idega.data.IDOAddRelationshipException,com.idega.data.IDORemoveRelationshipException;
 public void setMaxPerUnit(int p0);
 public void setNumberOfUnits(int p0);
 public void setPrimaryKey(java.lang.Object p0);
 public void setRating(float p0);
 public void setRoomTypeIds(int[] p0)throws com.idega.data.IDORemoveRelationshipException,com.idega.data.IDOAddRelationshipException;
}
