package com.idega.block.school.data;


public interface SchoolUserInfoHome extends com.idega.data.IDOHome
{
 public SchoolUserInfo create() throws javax.ejb.CreateException;
 public SchoolUserInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException;

}