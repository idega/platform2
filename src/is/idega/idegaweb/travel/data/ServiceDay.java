package is.idega.idegaweb.travel.data;


public interface ServiceDay extends com.idega.data.IDOEntity
{
 public int getDayOfWeek();
 public int getEstimated();
 public java.lang.String getIDColumnName();
 public int getMax();
 public int getMin();
 public java.lang.Class getPrimaryKeyClass();
 //public is.idega.idegaweb.travel.data.ServiceDay getServiceDay(int p0,com.idega.util.IWTimestamp p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 //public is.idega.idegaweb.travel.data.ServiceDay getServiceDay(int p0,int p1)throws javax.ejb.FinderException,java.rmi.RemoteException;
 public java.util.Collection getServiceDays(int p0)throws javax.ejb.FinderException;
 public is.idega.idegaweb.travel.data.ServiceDay[] getServiceDaysOfWeek(int p0)throws java.sql.SQLException;
 public int getServiceId();
 public void initializeAttributes();
 public void setDayOfWeek(int p0,int p1,int p2,int p3,int p4);
 public void setDayOfWeek(int p0);
 public void setDayOfWeek(int p0,int p1);
 public void setEstimated(int p0);
 public void setMax(int p0);
 public void setMin(int p0);
 public void setServiceId(int p0);
}
