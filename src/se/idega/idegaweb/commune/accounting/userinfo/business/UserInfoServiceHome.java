package se.idega.idegaweb.commune.accounting.userinfo.business;


public interface UserInfoServiceHome extends com.idega.business.IBOHome
{
 public UserInfoService create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}