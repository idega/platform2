package com.idega.block.building.data;


public class ApartmentHomeImpl extends com.idega.data.IDOFactory implements ApartmentHome
{
 protected Class getEntityInterfaceClass(){
  return Apartment.class;
 }


 public Apartment create() throws javax.ejb.CreateException{
  return (Apartment) super.createIDO();
 }

public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindByName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySQL(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindBySQL(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySearch(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.Integer p4,boolean p5)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindBySearch(p0,p1,p2,p3,p4,p5);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByType(java.lang.Integer p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindByType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findeByTypeAndComplex(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((ApartmentBMPBean)entity).ejbFindeByTypeAndComplex(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
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

public int getRentableCount()throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ApartmentBMPBean)entity).ejbHomeGetRentableCount();
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

public int getTypeAndComplexCount(java.lang.Integer p0,java.lang.Integer p1)throws com.idega.data.IDOException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	int theReturn = ((ApartmentBMPBean)entity).ejbHomeGetTypeAndComplexCount(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return theReturn;
}

}