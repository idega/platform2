package se.idega.idegaweb.commune.complaint.data;


public interface Complaint extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public java.lang.String getAnswer() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
 public java.lang.String[] getCaseStatusDescriptions() throws java.rmi.RemoteException;
 public java.lang.String[] getCaseStatusKeys() throws java.rmi.RemoteException;
 public java.lang.String getComplaint() throws java.rmi.RemoteException;
 public com.idega.block.process.data.CaseCode getComplaintCaseType() throws java.rmi.RemoteException;
 public java.lang.String getComplaintType() throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public int getManagerID() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setAnswer(java.lang.String p0) throws java.rmi.RemoteException;
 public void setComplaint(java.lang.String p0) throws java.rmi.RemoteException;
 public void setComplaintType(java.lang.String p0) throws java.rmi.RemoteException;
 public void setComplaintType(com.idega.block.process.data.CaseCode p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public void setManagerID(int p0) throws java.rmi.RemoteException;
 public void setManagerID(java.lang.Integer p0) throws java.rmi.RemoteException;
}
