package com.idega.block.building.data;


public class BuildingHomeImpl extends com.idega.data.IDOFactory implements BuildingHome
{
 protected Class getEntityInterfaceClass(){
  return Building.class;
 }


 public Building create() throws javax.ejb.CreateException{
  return (Building) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BuildingBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByComplex(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BuildingBMPBean)entity).ejbFindByComplex(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByComplex(com.idega.block.building.data.Complex p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((BuildingBMPBean)entity).ejbFindByComplex(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


 public Building findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (Building) super.findByPrimaryKeyIDO(pk);
 }


public java.util.Collection getImageFilesByComplex(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection theReturn = ((BuildingBMPBean)entity).ejbHomeGetImageFilesByComplex(p0);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}



}