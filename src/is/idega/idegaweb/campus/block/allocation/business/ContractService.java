package is.idega.idegaweb.campus.block.allocation.business;

import com.idega.block.application.data.Applicant;


public interface ContractService extends com.idega.business.IBOService
{
	public is.idega.idegaweb.campus.block.allocation.data.Contract allocate(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.util.Date p3,java.util.Date p4)throws is.idega.idegaweb.campus.block.allocation.business.AllocationException, java.rmi.RemoteException;
	 public void changeApplicationStatus(is.idega.idegaweb.campus.block.allocation.data.Contract p0)throws java.lang.Exception, java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.allocation.data.Contract createNewContract(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.util.Date p3,java.util.Date p4)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
	 public com.idega.user.data.User createNewUser(com.idega.block.application.data.Applicant p0,java.lang.String[] p1)throws java.rmi.RemoteException,javax.ejb.CreateException, java.rmi.RemoteException;
	 public void createUserLogin(java.lang.Integer p0,java.lang.Integer p1,java.lang.String p2,java.lang.String p3,boolean p4)throws java.lang.Exception, java.rmi.RemoteException;
	 public boolean deleteAllocation(java.lang.Integer p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
	 public void deleteFromWaitingList(is.idega.idegaweb.campus.block.allocation.data.Contract p0) throws java.rmi.RemoteException;
	 public void deliverKey(java.lang.Integer p0,java.sql.Timestamp p1) throws java.rmi.RemoteException;
	 public void deliverKey(java.lang.Integer p0) throws java.rmi.RemoteException;
	 public boolean doGarbageContract(java.lang.Integer p0) throws java.rmi.RemoteException;
	 public void endContract(java.lang.Integer p0,com.idega.util.IWTimestamp p1,java.lang.String p2,boolean p3) throws java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods getApartmentTypePeriod(java.lang.Integer p0) throws java.rmi.RemoteException;
	 public java.util.Map getApplicantContractsByStatus(java.lang.String p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.application.business.ApplicationService getApplicationService()throws java.rmi.RemoteException, java.rmi.RemoteException;
	 public java.util.Map getAvailableApartmentDates(java.lang.Integer p0,java.lang.Integer p1)throws javax.ejb.FinderException, java.rmi.RemoteException;
	 public com.idega.block.building.business.BuildingService getBuildingService()throws java.rmi.RemoteException, java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.allocation.data.ContractHome getContractHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
	 public com.idega.util.IWTimestamp[] getContractStampsForApartment(java.lang.Integer p0)throws javax.ejb.FinderException,java.rmi.RemoteException, java.rmi.RemoteException;
	 public com.idega.util.IWTimestamp[] getContractStampsForApartment(com.idega.block.building.data.Apartment p0) throws java.rmi.RemoteException;
	 public com.idega.util.IWTimestamp[] getContractStampsFromPeriod(is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods p0,java.lang.Integer p1) throws java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.allocation.data.ContractTextHome getContractTextHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
	 public java.lang.String getLocalizedStatus(com.idega.idegaweb.IWResourceBundle p0,java.lang.String p1) throws java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.mailinglist.business.MailingListService getMailingListService()throws java.rmi.RemoteException, java.rmi.RemoteException;
	 public java.util.Map getNewApplicantContracts()throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
	 public java.util.Date getNextAvailableDate(com.idega.block.building.data.Apartment p0) throws java.rmi.RemoteException;
	 public com.idega.user.business.UserBusiness getUserService()throws java.rmi.RemoteException, java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.allocation.business.Period getValidPeriod(is.idega.idegaweb.campus.block.allocation.data.Contract p0,com.idega.block.building.data.Apartment p1,java.lang.Integer p2,java.lang.Integer p3) throws java.rmi.RemoteException;
	 public is.idega.idegaweb.campus.block.application.data.WaitingListHome getWaitingListHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
	 public void reactivateWaitingList(java.lang.Integer p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
	 public void removeWaitingList(java.lang.Integer p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
	 public void resetWaitingListRejection(java.lang.Integer p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
	 public void resignContract(java.lang.Integer p0,com.idega.util.IWTimestamp p1,java.lang.String p2,boolean p3) throws java.rmi.RemoteException;
	 public void returnKey(com.idega.idegaweb.IWApplicationContext p0,java.lang.Integer p1) throws java.rmi.RemoteException;
	 public java.lang.String signContract(java.lang.Integer p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,boolean p5,boolean p6,boolean p7,boolean p8,boolean p9,com.idega.idegaweb.IWResourceBundle p10,java.lang.String p11,java.lang.String p12) throws java.rmi.RemoteException;
	 public void deleteFromWaitingList(Applicant applicant);
	 public void endExpiredContracts();
}
