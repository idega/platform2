package is.idega.idegaweb.travel.service.hotel.data;


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


public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((HotelBMPBean)entity).ejbHomeFind(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}


}