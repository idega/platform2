package is.idega.idegaweb.member.data;


public interface GroupApplication extends com.idega.data.IDOEntity
{
 public java.lang.String getAdminComment() throws java.rmi.RemoteException;
 public int getUserId() throws java.rmi.RemoteException;
 public int getApplicationGroupId() throws java.rmi.RemoteException;
 public void removeAllGroups()throws com.idega.data.IDORemoveRelationshipException, java.rmi.RemoteException;
 public java.lang.String getUserComment() throws java.rmi.RemoteException;
 public java.sql.Timestamp getModified() throws java.rmi.RemoteException;
 public void setApplicationGroupId(int p0) throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void addGroup(com.idega.user.data.Group p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public void setStatus(java.lang.String p0) throws java.rmi.RemoteException;
 public void addGroups(java.util.List p0)throws com.idega.data.IDOAddRelationshipException, java.rmi.RemoteException;
 public void setModified(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public com.idega.user.data.User getUser() throws java.rmi.RemoteException;
 public java.sql.Timestamp getCreated() throws java.rmi.RemoteException;
 public java.lang.String getStatus() throws java.rmi.RemoteException;
 public void setUserComment(java.lang.String p0) throws java.rmi.RemoteException;
 public void setAdminComment(java.lang.String p0) throws java.rmi.RemoteException;
 public java.util.Collection getGroups() throws java.rmi.RemoteException;
 public void setUserId(int p0) throws java.rmi.RemoteException;
 public void setCreated(java.sql.Timestamp p0) throws java.rmi.RemoteException;
}
