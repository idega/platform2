package is.idega.idegaweb.travel.data;

import javax.ejb.*;

public interface ServiceDay extends com.idega.data.IDOLegacyEntity
{
 public int getDayOfWeek();
 public int[] getDaysOfWeek(int p0);
 public int getEstimated();
 public java.lang.String getIDColumnName();
 public int getMax();
 public int getMin();
 public is.idega.idegaweb.travel.data.ServiceDay getServiceDay(int p0,com.idega.util.idegaTimestamp p1)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public is.idega.idegaweb.travel.data.ServiceDay getServiceDay(int p0,int p1)throws java.rmi.RemoteException,javax.ejb.FinderException;
 public java.util.Collection getServiceDays(int p0)throws javax.ejb.FinderException;
 public is.idega.idegaweb.travel.data.ServiceDay[] getServiceDaysOfWeek(int p0)throws java.sql.SQLException;
 public int getServiceId();
 public void setDayOfWeek(int p0,int p1);
 public void setDayOfWeek(int p0);
 public void setDayOfWeek(int p0,int p1,int p2,int p3,int p4);
 public void setEstimated(int p0);
 public void setMax(int p0);
 public void setMin(int p0);
 public void setServiceId(int p0);
 public void setServiceWithNoDays(int p0)throws java.sql.SQLException;
}
