package com.idega.block.school.data;


public class SchoolClassMemberHomeImpl extends com.idega.data.IDOFactory implements SchoolClassMemberHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolClassMember.class;
 }


 public SchoolClassMember create() throws javax.ejb.CreateException{
  return (SchoolClassMember) super.createIDO();
 }


public java.util.Collection findAllBySeasonAndMaximumAge(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindAllBySeasonAndMaximumAge(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByUserAndSeason(int p0,int p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindAllByUserAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findAllByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindAllByUserAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindBySchool(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchool(int p0,int p1,java.sql.Date p2)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindBySchool(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchool(int p0,int p1,java.sql.Date p2,boolean p3)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindBySchool(p0,p1,p2,p3);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolAndSeason(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindBySchoolAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolAndSeasonAndYear(int p0,int p1,int p2)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindBySchoolAndSeasonAndYear(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolClass(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindBySchoolClass(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findBySchoolClass(com.idega.block.school.data.SchoolClass p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindBySchoolClass(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStudent(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindByStudent(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStudent(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindByStudent(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStudentAndSchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindByStudentAndSchool(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByStudentAndTypes(int p0,java.util.Collection p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindByStudentAndTypes(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public SchoolClassMember findByUserAndSchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSchool(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSchoolAndSeason(int p0,int p1,int p2)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSchoolAndSeason(p0,p1,p2);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSchoolClass(com.idega.user.data.User p0,com.idega.block.school.data.SchoolClass p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSchoolClass(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSchoolClass(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSchoolClass(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSeason(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findLatestByUserAndSchool(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindLatestByUserAndSchool(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolClassMember) super.findByPrimaryKeyIDO(pk);
 }



}