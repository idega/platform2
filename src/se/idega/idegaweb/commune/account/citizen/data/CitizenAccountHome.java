package se.idega.idegaweb.commune.account.citizen.data;


public interface CitizenAccountHome extends com.idega.data.IDOHome
{
 public CitizenAccount create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public CitizenAccount findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllCasesByStatus(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllCasesByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;

}