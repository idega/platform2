package is.idega.idegaweb.campus.block.allocation.business;


public interface ContractService extends com.idega.business.IBOService
{
 public void changeApplicationStatus(is.idega.idegaweb.campus.block.allocation.data.Contract p0)throws java.lang.Exception, java.rmi.RemoteException;
 public void createUserLogin(int p0,int p1,java.lang.String p2,java.lang.String p3,boolean p4)throws java.lang.Exception, java.rmi.RemoteException;
 public boolean deleteAllocation(java.lang.Integer p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public void deleteFromWaitingList(is.idega.idegaweb.campus.block.allocation.data.Contract p0) throws java.rmi.RemoteException;
 public void deliverKey(com.idega.idegaweb.IWApplicationContext p0,java.lang.Integer p1,java.sql.Timestamp p2) throws java.rmi.RemoteException;
 public void deliverKey(com.idega.idegaweb.IWApplicationContext p0,java.lang.Integer p1) throws java.rmi.RemoteException;
 public boolean doGarbageContract(java.lang.Integer p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.allocation.data.Contract endContract(java.lang.Integer p0,com.idega.util.IWTimestamp p1,java.lang.String p2,boolean p3) throws java.rmi.RemoteException;
 public com.idega.block.finance.business.AccountBusiness getAccountService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.allocation.data.ContractHome getContractHome()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.util.IWTimestamp[] getContractStampsForApartment(int p0) throws java.rmi.RemoteException;
 public com.idega.util.IWTimestamp[] getContractStampsForApartment(com.idega.block.building.data.Apartment p0) throws java.rmi.RemoteException;
 public com.idega.util.IWTimestamp[] getContractStampsFromPeriod(is.idega.idegaweb.campus.block.building.data.ApartmentTypePeriods p0,int p1) throws java.rmi.RemoteException;
 public java.lang.String getLocalizedStatus(com.idega.idegaweb.IWResourceBundle p0,java.lang.String p1) throws java.rmi.RemoteException;
 public com.idega.idegaweb.IWResourceBundle getResourceBundle(com.idega.idegaweb.IWUserContext p0) throws java.rmi.RemoteException;
 public com.idega.user.business.UserBusiness getUserService()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean makeNewContract(com.idega.idegaweb.IWApplicationContext p0,com.idega.user.data.User p1,com.idega.block.application.data.Applicant p2,java.lang.Integer p3,com.idega.util.IWTimestamp p4,com.idega.util.IWTimestamp p5)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public com.idega.user.data.User makeNewUser(com.idega.block.application.data.Applicant p0,java.lang.String[] p1) throws java.rmi.RemoteException;
 public void resignContract(com.idega.idegaweb.IWApplicationContext p0,java.lang.Integer p1,com.idega.util.IWTimestamp p2,java.lang.String p3,boolean p4) throws java.rmi.RemoteException;
 public void returnKey(com.idega.idegaweb.IWApplicationContext p0,java.lang.Integer p1) throws java.rmi.RemoteException;
 public java.lang.String signContract(com.idega.idegaweb.IWUserContext p0,java.lang.Integer p1,java.lang.Integer p2,java.lang.Integer p3,java.lang.String p4,boolean p5,boolean p6,boolean p7,boolean p8,boolean p9,java.lang.String p10,java.lang.String p11) throws java.rmi.RemoteException;
 public is.idega.idegaweb.campus.block.allocation.data.Contract updateAllocation(java.lang.Integer p0,java.lang.Integer p1,java.sql.Date p2,java.sql.Date p3) throws java.rmi.RemoteException;
}
