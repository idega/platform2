package se.idega.idegaweb.commune.message.business;


public interface MessageSessionHome extends com.idega.business.IBOHome
{
 public MessageSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}