package com.idega.block.dataquery.data;


public interface UserQueryHome extends com.idega.data.IDOHome
{
 public UserQuery create() throws javax.ejb.CreateException;
 public UserQuery findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException;
 public java.util.Collection findByGroup(com.idega.user.data.Group p0)throws javax.ejb.FinderException;
 public java.util.Collection findByGroupAndPermission(com.idega.user.data.Group p0,java.lang.String p1)throws javax.ejb.FinderException;

}