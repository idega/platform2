package is.idega.idegaweb.travel.data;


public interface ServiceHome extends com.idega.data.IDOHome
{
 public Service create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Service findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}