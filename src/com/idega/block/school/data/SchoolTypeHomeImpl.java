package com.idega.block.school.data;


public class SchoolTypeHomeImpl extends com.idega.data.IDOFactory implements SchoolTypeHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolType.class;
 }


 public SchoolType create() throws javax.ejb.CreateException{
  return (SchoolType) super.createIDO();
 }


public java.util.Collection findAllByCategory(java.lang.String p0,boolean p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllByCategory(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByCategory(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllFreetimeTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllFreetimeTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllFreetimeTypes(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllFreetimeTypes(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllSchoolTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllSchoolTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolType findByTypeKey(java.lang.String p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolTypeBMPBean)entity).ejbFindByTypeKey(p0);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolType) super.findByPrimaryKeyIDO(pk);
 }



}