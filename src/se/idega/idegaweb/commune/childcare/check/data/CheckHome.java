package se.idega.idegaweb.commune.childcare.check.data;


public interface CheckHome extends com.idega.data.IDOHome
{
 public Check create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Check findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllCasesByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findApprovedChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChecks()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChecksByUser(com.idega.user.data.User p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findChecksByUserAndStatus(com.idega.user.data.User p0,java.lang.String p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findNonApprovedChecks()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
	public Check findCheckForChild(int p0) throws javax.ejb.FinderException;
}