package com.idega.block.building.data;


public class ApartmentHomeImpl extends com.idega.data.IDOFactory implements ApartmentHome
{
 protected Class getEntityInterfaceClass(){
  return Apartment.class;
 }


 public Apartment create() throws javax.ejb.CreateException{
  return (Apartment) super.createIDO();
 }


public java.util.Collection findByFloor(com.idega.block.building.data.Floor p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindByFloor(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByFloor(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindByFloor(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Apartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Apartment) super.findByPrimaryKeyIDO(pk);
 }



}