package is.idega.idegaweb.campus.block.request.data;


public interface RequestHome extends com.idega.data.IDOHome
{
 public Request create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Request findByPrimaryKey(int id) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public Request findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}