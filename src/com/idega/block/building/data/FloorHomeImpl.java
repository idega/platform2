package com.idega.block.building.data;


public class FloorHomeImpl extends com.idega.data.IDOFactory implements FloorHome
{
 protected Class getEntityInterfaceClass(){
  return Floor.class;
 }


 public Floor create() throws javax.ejb.CreateException{
  return (Floor) super.createIDO();
 }


public java.util.Collection findByBuilding(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FloorBMPBean)entity).ejbFindByBuilding(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByBuilding(com.idega.block.building.data.Building p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((FloorBMPBean)entity).ejbFindByBuilding(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public Floor findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Floor) super.findByPrimaryKeyIDO(pk);
 }



}