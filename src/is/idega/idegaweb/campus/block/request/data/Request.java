package is.idega.idegaweb.campus.block.request.data;

import javax.ejb.*;

public interface Request extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getDateProcessed() throws java.rmi.RemoteException;
 public int getUserId() throws java.rmi.RemoteException;
 public void setDateSent(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public java.lang.String getRequestType() throws java.rmi.RemoteException;
 public void setSpecialTime(java.lang.String p0) throws java.rmi.RemoteException;
 public void setDateProcessed(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setStatus(java.lang.String p0) throws java.rmi.RemoteException;
 public void setDescription(java.lang.String p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getDateFailure() throws java.rmi.RemoteException;
 public java.lang.String getDescription() throws java.rmi.RemoteException;
 public java.lang.String getStatus() throws java.rmi.RemoteException;
 public void setRequestType(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.String getSpecialTime() throws java.rmi.RemoteException;
 public void setUserId(java.lang.Integer p0) throws java.rmi.RemoteException;
 public java.sql.Timestamp getDateSent() throws java.rmi.RemoteException;
 public void setDateFailure(java.sql.Timestamp p0) throws java.rmi.RemoteException;
 public void setUserId(int p0) throws java.rmi.RemoteException;
}
