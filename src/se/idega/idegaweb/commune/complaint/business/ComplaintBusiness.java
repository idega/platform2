package se.idega.idegaweb.commune.complaint.business;


public interface ComplaintBusiness extends com.idega.business.IBOService,com.idega.block.process.business.CaseBusiness
{
 public void answerComplaint(int p0,java.lang.String p1,com.idega.user.data.User p2)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void createComplaint(java.lang.String p0,java.lang.String p1,com.idega.block.process.data.CaseCode p2,com.idega.user.data.User p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findAllComplaintTypes()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findComplaints()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findComplaintsByStatus(com.idega.block.process.data.CaseStatus p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findComplaintsByType(com.idega.block.process.data.CaseCode p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findComplaintsForManager(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.util.Collection findUserComplaints(com.idega.user.data.User p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void forwardComplaint(int p0,com.idega.block.process.data.CaseCode p1,com.idega.user.data.User p2)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.complaint.data.Complaint getComplaint(int p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void sendMessageToCitizen(se.idega.idegaweb.commune.complaint.data.Complaint p0,int p1,java.lang.String p2,java.lang.String p3)throws javax.ejb.CreateException,java.rmi.RemoteException, java.rmi.RemoteException;
}
