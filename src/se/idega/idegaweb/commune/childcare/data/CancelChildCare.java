package se.idega.idegaweb.commune.childcare.data;

import javax.ejb.*;

public interface CancelChildCare extends com.idega.data.IDOEntity,com.idega.block.process.data.Case
{
 public java.lang.String getReason() throws java.rmi.RemoteException;
 public void setCheckId(int p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeDescription() throws java.rmi.RemoteException;
 public void setReason(java.lang.String p0) throws java.rmi.RemoteException;
 public int getCheckId() throws java.rmi.RemoteException;
 public java.sql.Date getCancellationDate() throws java.rmi.RemoteException;
 public void initializeAttributes() throws java.rmi.RemoteException;
 public void setCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check getCheck() throws java.rmi.RemoteException;
 public void setCancellationDate(java.sql.Date p0) throws java.rmi.RemoteException;
 public java.lang.String getCaseCodeKey() throws java.rmi.RemoteException;
}
