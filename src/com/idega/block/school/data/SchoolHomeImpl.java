package com.idega.block.school.data;


public class SchoolHomeImpl extends com.idega.data.IDOFactory implements SchoolHome
{
 protected Class getEntityInterfaceClass(){
  return School.class;
 }


 public School create() throws javax.ejb.CreateException{
  return (School) super.createIDO();
 }


public java.util.Collection findAllByAreaAndType(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolBMPBean)entity).ejbFindAllByAreaAndType(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolArea(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolArea(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolGroup(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolType(int p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolType(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllBySchoolName(String name)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolBMPBean)entity).ejbFindAllBySchoolName(name);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


public java.util.Collection findAllSchools()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolBMPBean)entity).ejbFindAllSchools();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public School findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (School) super.findByPrimaryKeyIDO(pk);
 }


}