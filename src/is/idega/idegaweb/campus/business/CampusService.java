package is.idega.idegaweb.campus.business;


public interface CampusService extends com.idega.business.IBOService
{
 public is.idega.idegaweb.campus.business.CampusSettings getCampusSettings() throws java.rmi.RemoteException;
 public void storeSettings(is.idega.idegaweb.campus.business.CampusSettings p0) throws java.rmi.RemoteException;
}
