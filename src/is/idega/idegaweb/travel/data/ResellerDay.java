package is.idega.idegaweb.travel.data;



public interface ResellerDay extends com.idega.data.IDOEntity
{
 public void deleteReseller(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.RemoveException;
 public void deleteService(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException,javax.ejb.RemoveException;
 public int getDayOfWeek();
 //public Collection getDaysOfWeek(int resellerId, int serviceId) throws FinderException;
 //public int[] getDaysOfWeek(int p0,int p1)throws java.rmi.RemoteException;
 public java.lang.Class getPrimaryKeyClass();
 public int getResellerId();
 public int getServiceId();
 public void setDayOfWeek(int p0,int p1,int p2);
 public void setDayOfWeek(int p0);
 public void setResellerId(int p0);
 public void setServiceId(int p0);
}
