package se.idega.idegaweb.commune.childcare.business;


public interface ChildCareBusinessHome extends com.idega.business.IBOHome
{
 public ChildCareBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}