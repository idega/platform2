package com.idega.block.school.data;


public class SchoolDepartmentHomeImpl extends com.idega.data.IDOFactory implements SchoolDepartmentHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolDepartment.class;
 }


 public SchoolDepartment create() throws javax.ejb.CreateException{
  return (SchoolDepartment) super.createIDO();
 }


public java.util.Collection findAllDepartmentsBySchool(com.idega.block.school.data.School p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolDepartmentBMPBean)entity).ejbFindAllDepartmentsBySchool(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllDepartmentsBySchool(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolDepartmentBMPBean)entity).ejbFindAllDepartmentsBySchool(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllDepartmentsByUser(com.idega.block.school.data.SchoolUser p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolDepartmentBMPBean)entity).ejbFindAllDepartmentsByUser(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public SchoolDepartment findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolDepartment) super.findByPrimaryKeyIDO(pk);
 }



}