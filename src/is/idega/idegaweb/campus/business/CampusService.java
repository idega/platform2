package is.idega.idegaweb.campus.business;


public interface CampusService extends com.idega.business.IBOService
{
 public is.idega.idegaweb.campus.block.application.business.ApplicationService getApplicationService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.block.building.business.BuildingService getBuildingService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.campus.business.CampusSettings getCampusSettings() throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.allocation.business.ContractService getContractService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public void storeSettings(is.idega.idegaweb.campus.business.CampusSettings p0) throws java.rmi.RemoteException;
}
