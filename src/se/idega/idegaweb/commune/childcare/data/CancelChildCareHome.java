package se.idega.idegaweb.commune.childcare.data;


public interface CancelChildCareHome extends com.idega.data.IDOHome
{
 public CancelChildCare create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public CancelChildCare findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}