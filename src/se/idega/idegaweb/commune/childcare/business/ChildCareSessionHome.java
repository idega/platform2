package se.idega.idegaweb.commune.childcare.business;


public interface ChildCareSessionHome extends com.idega.business.IBOHome
{
 public ChildCareSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}