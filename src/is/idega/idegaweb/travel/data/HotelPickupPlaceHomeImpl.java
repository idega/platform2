package is.idega.idegaweb.travel.data;


public class HotelPickupPlaceHomeImpl extends com.idega.data.IDOFactory implements HotelPickupPlaceHome
{
 protected Class getEntityInterfaceClass(){
  return HotelPickupPlace.class;
 }


 public HotelPickupPlace create() throws javax.ejb.CreateException{
  return (HotelPickupPlace) super.createIDO();
 }


public java.util.Collection findHotelPickupPlaces(is.idega.idegaweb.travel.data.Service p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HotelPickupPlaceBMPBean)entity).ejbFindHotelPickupPlaces(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findHotelPickupPlaces(com.idega.block.trade.stockroom.data.Supplier p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((HotelPickupPlaceBMPBean)entity).ejbFindHotelPickupPlaces(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public HotelPickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (HotelPickupPlace) super.findByPrimaryKeyIDO(pk);
 }


public void removeFromAllServices()throws com.idega.data.IDORemoveRelationshipException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	((HotelPickupPlaceBMPBean)entity).ejbHomeRemoveFromAllServices();
	this.idoCheckInPooledEntity(entity);
//	return theReturn;
}


}