package is.idega.idegaweb.travel.data;


public class PickupPlaceHomeImpl extends com.idega.data.IDOFactory implements PickupPlaceHome
{
 protected Class getEntityInterfaceClass(){
  return PickupPlace.class;
 }


 public PickupPlace create() throws javax.ejb.CreateException{
  return (PickupPlace) super.createIDO();
 }


public java.util.Collection findAllPlaces(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PickupPlaceBMPBean)entity).ejbFindAllPlaces(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findDropoffPlaces(com.idega.block.trade.stockroom.data.Supplier p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PickupPlaceBMPBean)entity).ejbFindDropoffPlaces(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findDropoffPlaces(is.idega.idegaweb.travel.data.Service p0)throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PickupPlaceBMPBean)entity).ejbFindDropoffPlaces(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findHotelPickupPlaces(is.idega.idegaweb.travel.data.Service p0,int p1)throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PickupPlaceBMPBean)entity).ejbFindHotelPickupPlaces(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findHotelPickupPlaces(com.idega.block.trade.stockroom.data.Supplier p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PickupPlaceBMPBean)entity).ejbFindHotelPickupPlaces(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findHotelPickupPlaces(com.idega.block.trade.stockroom.data.Supplier p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PickupPlaceBMPBean)entity).ejbFindHotelPickupPlaces(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findHotelPickupPlaces(is.idega.idegaweb.travel.data.Service p0)throws java.rmi.RemoteException,javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((PickupPlaceBMPBean)entity).ejbFindHotelPickupPlaces(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public PickupPlace findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (PickupPlace) super.findByPrimaryKeyIDO(pk);
 }



}