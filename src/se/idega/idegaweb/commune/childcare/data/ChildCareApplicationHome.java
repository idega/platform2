package se.idega.idegaweb.commune.childcare.data;


public interface ChildCareApplicationHome extends com.idega.data.IDOHome
{
 public ChildCareApplication create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public ChildCareApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}