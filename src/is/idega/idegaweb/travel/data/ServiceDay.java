package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface ServiceDay extends com.idega.data.IDOEntity
{
 public int getDayOfWeek() throws java.rmi.RemoteException;
 public int[] getDaysOfWeek(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public int getEstimated() throws java.rmi.RemoteException;
 public java.lang.String getIDColumnName() throws java.rmi.RemoteException;
 public int getMax() throws java.rmi.RemoteException;
 public int getMin() throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.ServiceDay getServiceDay(int p0,com.idega.util.idegaTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.ServiceDay getServiceDay(int p0,int p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getServiceDays(int p0)throws javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.data.ServiceDay[] getServiceDaysOfWeek(int p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public int getServiceId() throws java.rmi.RemoteException;
 public void setDayOfWeek(int p0,int p1) throws java.rmi.RemoteException;
 public void setDayOfWeek(int p0) throws java.rmi.RemoteException;
 public void setDayOfWeek(int p0,int p1,int p2,int p3,int p4) throws java.rmi.RemoteException;
 public void setEstimated(int p0) throws java.rmi.RemoteException;
 public void setMax(int p0) throws java.rmi.RemoteException;
 public void setMin(int p0) throws java.rmi.RemoteException;
 public void setServiceId(int p0) throws java.rmi.RemoteException;
}
