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

 public SchoolArea findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolArea) super.findByPrimaryKeyIDO(pk);
 }



}