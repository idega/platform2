package is.idega.idegaweb.travel.service.hotel.data;


public class RoomTypeHomeImpl extends com.idega.data.IDOFactory implements RoomTypeHome
{
 protected Class getEntityInterfaceClass(){
  return RoomType.class;
 }


 public RoomType create() throws javax.ejb.CreateException{
  return (RoomType) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((RoomTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public RoomType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (RoomType) super.findByPrimaryKeyIDO(pk);
 }



}