package se.idega.idegaweb.commune.childcare.check.data;


public interface CheckHome extends com.idega.data.IDOHome
{
 public Check create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Check findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChecks()throws javax.ejb.FinderException, java.rmi.RemoteException;

}