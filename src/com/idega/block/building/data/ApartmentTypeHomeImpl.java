package com.idega.block.building.data;


public class ApartmentTypeHomeImpl extends com.idega.data.IDOFactory implements ApartmentTypeHome
{
 protected Class getEntityInterfaceClass(){
  return ApartmentType.class;
 }


 public ApartmentType create() throws javax.ejb.CreateException{
  return (ApartmentType) super.createIDO();
 }

public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypeBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByBuilding(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypeBMPBean)entity).ejbFindByBuilding(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByCategory(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypeBMPBean)entity).ejbFindByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByComplex(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypeBMPBean)entity).ejbFindByComplex(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findFromSameComplex(com.idega.block.building.data.ApartmentType p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentTypeBMPBean)entity).ejbFindFromSameComplex(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public ApartmentType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (ApartmentType) super.findByPrimaryKeyIDO(pk);
 }



}