package is.idega.experimental.ibotest;


public interface SimpleServiceHome extends com.idega.business.IBOHome
{
 public SimpleService create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}