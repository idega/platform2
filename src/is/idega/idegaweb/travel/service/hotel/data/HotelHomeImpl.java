package is.idega.idegaweb.travel.service.hotel.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.util.IWTimestamp;


public class HotelHomeImpl extends com.idega.data.IDOFactory implements HotelHome
{
 protected Class getEntityInterfaceClass(){
  return Hotel.class;
 }


 public Hotel create() throws javax.ejb.CreateException{
  return (Hotel) super.createIDO();
 }


 public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Hotel) super.findByPrimaryKeyIDO(pk);
 }

 public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,java.lang.Object[] p4)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((HotelBMPBean)entity).ejbHomeFind(p0, p1, p2, p3, p4);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
 }

 public Collection find(IWTimestamp fromStamp, IWTimestamp toStamp, Object[] roomTypeId, Object[] hotelTypeId, Object[] postalCodeId, Object[] supplierId, float minRating, float maxRating) throws FinderException {
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((HotelBMPBean)entity).ejbHomeFind(fromStamp, toStamp, roomTypeId, hotelTypeId, postalCodeId, supplierId, minRating, maxRating);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}