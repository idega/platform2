package se.idega.idegaweb.commune.accounting.update.business;


public interface ChildCareContractPlacementBusiness extends com.idega.business.IBOService
{
 public se.idega.idegaweb.commune.childcare.data.ChildCareApplicationHome getChildCareApplicationHome() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.care.data.ChildCareContractHome getChildCareContractHome() throws java.rmi.RemoteException;
 public com.idega.block.school.business.SchoolBusiness getSchoolBusiness() throws java.rmi.RemoteException;
 public void updateMissingPlacements() throws java.rmi.RemoteException;
}
