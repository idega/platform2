package se.idega.idegaweb.commune.accounting.business;


public interface AccountingSessionHome extends com.idega.business.IBOHome
{
 public AccountingSession create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}