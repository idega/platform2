package is.idega.idegaweb.campus.data;

import java.rmi.RemoteException;


public interface AccountPhone extends com.idega.data.IDOEntity
{
 public java.lang.Integer getAccountId()throws RemoteException;
 public java.lang.String getPhoneNumber()throws RemoteException;
 public java.sql.Date getValidFrom()throws RemoteException;
 public java.sql.Date getValidTo()throws RemoteException;

}
