package se.idega.idegaweb.commune.accounting.business;


public interface AccountingBusinessHome extends com.idega.business.IBOHome
{
 public AccountingBusiness create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}