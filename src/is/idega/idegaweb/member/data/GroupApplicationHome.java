package is.idega.idegaweb.member.data;


public interface GroupApplicationHome extends com.idega.data.IDOHome
{
 public GroupApplication create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public GroupApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAllApplicationsByStatusOrderedByCreationDate(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllApplicationsByStatusAndApplicationGroupOrderedByCreationDate(java.lang.String p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllApplicationsByStatusAndApplicationGroup(java.lang.String p0,com.idega.user.data.Group p1)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllApplicationsByStatus(java.lang.String p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllApplications()throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.lang.String getPendingStatusString()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getApprovedStatusString()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getDeniedStatusString()throws java.rmi.RemoteException, java.rmi.RemoteException;

}