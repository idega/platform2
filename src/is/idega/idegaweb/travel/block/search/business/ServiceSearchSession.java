package is.idega.idegaweb.travel.block.search.business;



public interface ServiceSearchSession extends com.idega.business.IBOSession
{
 public java.util.Collection getProducts() throws java.rmi.RemoteException;
 public int getState() throws java.rmi.RemoteException;
 public void setProducts(java.util.Collection p0) throws java.rmi.RemoteException;
 public void setState(int p0) throws java.rmi.RemoteException;
}
