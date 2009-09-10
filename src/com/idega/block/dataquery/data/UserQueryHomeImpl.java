package com.idega.block.dataquery.data;


public class UserQueryHomeImpl extends com.idega.data.IDOFactory implements UserQueryHome
{
 protected Class getEntityInterfaceClass(){
  return UserQuery.class;
 }


 public UserQuery create() throws javax.ejb.CreateException{
  return (UserQuery) super.createIDO();
 }


public java.util.Collection findAll()throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserQueryBMPBean)entity).ejbFindAll();
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserQueryBMPBean)entity).ejbFindByGroup(p0);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

public java.util.Collection findByGroupAndPermission(com.idega.user.data.Group p0,java.lang.String p1)throws javax.ejb.FinderException{
	com.idega.data.IDOEntity entity = this.idoCheckOutPooledEntity();
	java.util.Collection ids = ((UserQueryBMPBean)entity).ejbFindByGroupAndPermission(p0,p1);
	this.idoCheckInPooledEntity(entity);
	return this.getEntityCollectionForPrimaryKeys(ids);
}

 public UserQuery findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserQuery) super.findByPrimaryKeyIDO(pk);
 }



}