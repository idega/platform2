package is.idega.idegaweb.campus.data;

import javax.ejb.*;

public interface ApplicationSubjectInfo extends com.idega.data.IDOEntity
{
 public java.sql.Timestamp getFirstChange() throws java.rmi.RemoteException;
 public java.sql.Timestamp getFirstSubmission() throws java.rmi.RemoteException;
 public java.sql.Timestamp getLastSubmission() throws java.rmi.RemoteException;
 public int getNumber() throws java.rmi.RemoteException;
 public java.lang.String getSubjectName() throws java.rmi.RemoteException;
 public java.sql.Timestamp getLastChange() throws java.rmi.RemoteException;
 public java.lang.String getStatus() throws java.rmi.RemoteException;
 public void delete()throws java.sql.SQLException, java.rmi.RemoteException;
 public int getSubjectId() throws java.rmi.RemoteException;
}
