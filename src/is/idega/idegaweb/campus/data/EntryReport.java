package is.idega.idegaweb.campus.data;


public interface EntryReport
{
 public void setEntityContext(javax.ejb.EntityContext p0) throws java.rmi.RemoteException;
 public java.lang.String getKeyName() throws java.rmi.RemoteException;
 public void unsetEntityContext() throws java.rmi.RemoteException;
 public java.lang.Class getPrimaryKeyClass() ;
 public int getKeyId() throws java.rmi.RemoteException;
 public int getBuildingId() throws java.rmi.RemoteException;
 public float getTotal() throws java.rmi.RemoteException;
 public java.lang.String getBuildingName() throws java.rmi.RemoteException;
 public void setEJBHome(javax.ejb.EJBHome p0) ;
 public java.lang.String getKeyInfo() throws java.rmi.RemoteException;
 public int getNumber() throws java.rmi.RemoteException;
}
