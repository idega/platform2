package se.idega.idegaweb.commune.account.provider.data;


public interface ProviderApplicationHome extends com.idega.data.IDOHome
{
 public ProviderApplication create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public ProviderApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}