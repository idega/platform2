package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.user.data.User;


public class SchoolClassMemberHomeImpl extends com.idega.data.IDOFactory implements SchoolClassMemberHome
{
 protected Class getEntityInterfaceClass(){
  return SchoolClassMember.class;
 }


 public SchoolClassMember create() throws javax.ejb.CreateException{
  return (SchoolClassMember) super.createIDO();
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

public java.util.Collection findAllBySeasonAndMaximumAge(int seasonID,int maxAge)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindAllBySeasonAndMaximumAge(seasonID,maxAge);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


public java.util.Collection findByStudent(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindByStudent(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}


public SchoolClassMember findByUserAndSchoolClass(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSchoolClass(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSchoolClass(com.idega.user.data.User p0,com.idega.block.school.data.SchoolClass p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSchoolClass(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSeason(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

public SchoolClassMember findByUserAndSeason(com.idega.user.data.User p0,com.idega.block.school.data.SchoolSeason p1)throws javax.ejb.FinderException,java.rmi.RemoteException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	Object pk = ((SchoolClassMemberBMPBean)entity).ejbFindByUserAndSeason(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.findByPrimaryKey(pk);
}

 public SchoolClassMember findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (SchoolClassMember) super.findByPrimaryKeyIDO(pk);
 }



	/**
	 * @see com.idega.block.school.data.SchoolClassMemberHome#findAllByUserAndSeason(int, int)
	 */
	public Collection findAllByUserAndSeason(int p0, int p1) throws FinderException,RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindAllByUserAndSeason(p0,p1);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberHome#findAllByUserAndSeason(com.idega.user.data.User, com.idega.block.school.data.SchoolSeason)
	 */
	public Collection findAllByUserAndSeason(User p0, SchoolSeason p1) throws FinderException,RemoteException {
		com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
		java.util.Collection ids = ((SchoolClassMemberBMPBean)entity).ejbFindAllByUserAndSeason(p0,p1);
		this.idoCheckInPooledEntity(entity);
		return this.getEntityCollectionForPrimaryKeys(ids);
	}

}