package is.idega.idegaweb.atvr.supplier.application.data;


public interface NewProductApplicationHome extends com.idega.data.IDOHome
{
 public NewProductApplication create() throws javax.ejb.CreateException;
 public NewProductApplication findByPrimaryKey(Object pk) throws javax.ejb.FinderException;
 public java.util.Collection findAll()throws javax.ejb.FinderException,java.rmi.RemoteException;

}