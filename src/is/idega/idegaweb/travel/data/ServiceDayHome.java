package is.idega.idegaweb.travel.data;


public interface ServiceDayHome extends com.idega.data.IDOHome
{
 public ServiceDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public ServiceDay create(is.idega.idegaweb.travel.data.ServiceDayPK p0)throws javax.ejb.CreateException;
 public ServiceDay findByPrimaryKey(is.idega.idegaweb.travel.data.ServiceDayPK p0)throws javax.ejb.FinderException;
 public ServiceDay findByServiceAndDay(int p0,int p1)throws javax.ejb.FinderException;
 public boolean deleteService(int p0)throws java.rmi.RemoteException,javax.ejb.RemoveException,javax.ejb.FinderException;
 public int[] getDaysOfWeek(int p0)throws java.rmi.RemoteException,javax.ejb.RemoveException,javax.ejb.FinderException;
 public boolean getIfDay(int p0,int p1);
 public boolean setServiceWithNoDays(int p0)throws java.rmi.RemoteException,javax.ejb.RemoveException,javax.ejb.FinderException;

}