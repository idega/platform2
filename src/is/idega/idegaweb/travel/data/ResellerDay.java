package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface ResellerDay extends com.idega.data.IDOEntity
{
 public java.lang.String getIDColumnName();
 public void setResellerId(int p0);
 public int[] getDaysOfWeek(int p0,int p1)throws java.rmi.RemoteException;
 public int getServiceId();
 public void initializeAttributes();
 public void setDayOfWeek(int p0);
 public void setDayOfWeek(int p0,int p1,int p2);
 public void deleteService(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.RemoveException;
 public int getResellerId();
 public void setServiceId(int p0);
 public void deleteReseller(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.RemoveException;
 public int getDayOfWeek();
}
