package is.idega.experimental.idotest;


public interface ResponseHome extends com.idega.data.IDOHome
{
 public Response create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Response findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}