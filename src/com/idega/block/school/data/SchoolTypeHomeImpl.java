package com.idega.block.school.data;


public class SchoolTypeHomeImpl extends com.idega.data.IDOFactory implements SchoolTypeHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolType.class;
 }


 public SchoolType create() throws javax.ejb.CreateException{
  return (SchoolType) super.createIDO();
 }


public java.util.Collection findAllSchoolTypes()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllSchoolTypes();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByCategory(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolTypeBMPBean)entity).ejbFindAllByCategory(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolType findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolType) super.findByPrimaryKeyIDO(pk);
 }



}