package com.idega.block.school.data;


public class SchoolYearHomeImpl extends com.idega.data.IDOFactory implements SchoolYearHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolYear.class;
 }


 public SchoolYear create() throws javax.ejb.CreateException{
  return (SchoolYear) super.createIDO();
 }


public SchoolYear findByYearName(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolYearBMPBean)entity).ejbFindByYearName(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public java.util.Collection findAllSchoolYears()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolYearBMPBean)entity).ejbFindAllSchoolYears();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByAge(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolYearBMPBean)entity).ejbFindAllByAge(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolYear findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolYear) super.findByPrimaryKeyIDO(pk);
 }



}