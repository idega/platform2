package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface ResellerDay extends com.idega.data.IDOEntity
{
 public void deleteReseller(int p0)throws javax.ejb.RemoveException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public void deleteService(int p0)throws javax.ejb.RemoveException,javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getDayOfWeek() throws java.rmi.RemoteException;
 public int[] getDaysOfWeek(int p0,int p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getIDColumnName() throws java.rmi.RemoteException;
 public int getResellerId() throws java.rmi.RemoteException;
 public int getServiceId() throws java.rmi.RemoteException;
 public void setDayOfWeek(int p0) throws java.rmi.RemoteException;
 public void setDayOfWeek(int p0,int p1,int p2) throws java.rmi.RemoteException;
 public void setResellerId(int p0) throws java.rmi.RemoteException;
 public void setServiceId(int p0) throws java.rmi.RemoteException;
}
