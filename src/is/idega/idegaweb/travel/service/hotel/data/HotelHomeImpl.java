package is.idega.idegaweb.travel.service.hotel.data;


public class HotelHomeImpl extends com.idega.data.IDOFactory implements HotelHome
{
 protected Class getEntityInterfaceClass(){
  return Hotel.class;
 }


 public Hotel create() throws javax.ejb.CreateException{
  return (Hotel) super.createIDO();
 }


public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,java.lang.Object[] p4,java.lang.Object[] p5,float p6,float p7,String p8)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HotelBMPBean)entity).ejbFind(p0,p1,p2,p3,p4,p5,p6,p7,p8);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection find(com.idega.util.IWTimestamp p0,com.idega.util.IWTimestamp p1,java.lang.Object[] p2,java.lang.Object[] p3,java.lang.Object[] p4,String p5)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HotelBMPBean)entity).ejbFind(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Hotel findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Hotel) super.findByPrimaryKeyIDO(pk);
 }



}