package com.idega.block.user.data;


public interface UserExtraInfoHome extends com.idega.data.IDOHome
{
 public UserExtraInfo create() throws javax.ejb.CreateException;
 public UserExtraInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}