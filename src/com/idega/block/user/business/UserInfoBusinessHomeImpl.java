package com.idega.block.user.business;


public class UserInfoBusinessHomeImpl extends com.idega.business.IBOHomeImpl implements UserInfoBusinessHome
{
 protected Class getBeanInterfaceClass(){
  return UserInfoBusiness.class;
 }


 public UserInfoBusiness create() throws javax.ejb.CreateException{
  return (UserInfoBusiness) super.createIBO();
 }



}