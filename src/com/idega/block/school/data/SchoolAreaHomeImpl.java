package com.idega.block.school.data;


public class SchoolAreaHomeImpl extends com.idega.data.IDOFactory implements SchoolAreaHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolArea.class;
 }


 public SchoolArea create() throws javax.ejb.CreateException{
  return (SchoolArea) super.createIDO();
 }


public java.util.Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolAreaBMPBean)entity).ejbFindAllBySchoolType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolTypeAndCity(int p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolAreaBMPBean)entity).ejbFindAllBySchoolTypeAndCity(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolTypeCityAndManagementTypes(int p0,java.lang.String p1,java.util.Collection p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolAreaBMPBean)entity).ejbFindAllBySchoolTypeCityAndManagementTypes(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolTypes(java.util.Collection p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolAreaBMPBean)entity).ejbFindAllBySchoolTypes(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllSchoolAreas()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolAreaBMPBean)entity).ejbFindAllSchoolAreas();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolArea findSchoolAreaByAreaName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolAreaBMPBean)entity).ejbFindSchoolAreaByAreaName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolArea findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolArea) super.findByPrimaryKeyIDO(pk);
 }



}