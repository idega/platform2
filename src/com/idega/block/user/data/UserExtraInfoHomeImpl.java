package com.idega.block.user.data;


public class UserExtraInfoHomeImpl extends com.idega.data.IDOFactory implements UserExtraInfoHome
{
 protected Class getEntityInterfaceClass(){
  return UserExtraInfo.class;
 }


 public UserExtraInfo create() throws javax.ejb.CreateException{
  return (UserExtraInfo) super.createIDO();
 }


 public UserExtraInfo findByPrimaryKey(Object pk) throws javax.ejb.FinderException{
  return (UserExtraInfo) super.findByPrimaryKeyIDO(pk);
 }



}