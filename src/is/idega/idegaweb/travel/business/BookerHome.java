package is.idega.idegaweb.travel.business;


public interface BookerHome extends com.idega.business.IBOHome
{
 public Booker create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}