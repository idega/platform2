package se.idega.idegaweb.commune.business;


public interface CommuneBusinessHome extends com.idega.business.IBOHome
{
 public CommuneBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}