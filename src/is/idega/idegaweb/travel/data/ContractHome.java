package is.idega.idegaweb.travel.data;


public interface ContractHome extends com.idega.data.IDOHome
{
 public Contract create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public Contract findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;

}