package se.idega.idegaweb.commune.accounting.userinfo.business;


public class UserInfoServiceHomeImpl extends com.idega.business.IBOHomeImpl implements UserInfoServiceHome
{
 protected Class getBeanInterfaceClass(){
  return UserInfoService.class;
 }


 public UserInfoService create() throws javax.ejb.CreateException{
  return (UserInfoService) super.createIBO();
 }



}