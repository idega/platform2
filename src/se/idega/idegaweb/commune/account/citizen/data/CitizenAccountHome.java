package se.idega.idegaweb.commune.account.citizen.data;


public interface CitizenAccountHome extends com.idega.data.IDOHome
{
 public CitizenAccount create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public CitizenAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}