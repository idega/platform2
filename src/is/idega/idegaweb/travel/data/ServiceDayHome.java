package is.idega.idegaweb.travel.data;


public interface ServiceDayHome extends com.idega.data.IDOHome
{
 public ServiceDay create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public ServiceDay findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public boolean deleteService(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean getIfDay(int p0,int p1) throws java.rmi.RemoteException;
 public boolean setServiceWithNoDays(int p0)throws javax.ejb.FinderException,javax.ejb.RemoveException,java.rmi.RemoteException, java.rmi.RemoteException;

}