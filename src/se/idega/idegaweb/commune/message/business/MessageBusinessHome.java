package se.idega.idegaweb.commune.message.business;


public interface MessageBusinessHome extends com.idega.business.IBOHome
{
 public MessageBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}