package is.idega.idegaweb.atvr.supplier.application.data;


public interface NewProductApplicationHome extends com.idega.data.IDOHome
{
 public NewProductApplication create() throws javax.ejb.CreateException, java.rmi.RemoteException;
 public NewProductApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection findAll()throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;

}