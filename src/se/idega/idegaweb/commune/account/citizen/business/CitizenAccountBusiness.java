package se.idega.idegaweb.commune.account.citizen.business;

import se.idega.idegaweb.commune.account.business.AccountBusiness;

import com.idega.core.location.data.Commune;


public interface CitizenAccountBusiness extends com.idega.business.IBOService, AccountBusiness
{
 public void acceptApplication(int p0,com.idega.user.data.User p1,boolean p2,boolean p3)throws java.rmi.RemoteException,javax.ejb.CreateException;
 public void changePasswordAndSendLetterOrEmail(com.idega.core.accesscontrol.data.LoginTable p0,com.idega.user.data.User p1,java.lang.String p2,boolean p3,boolean p4)throws javax.ejb.CreateException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantChildren[] findCitizenApplicantChildren(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantCohabitant findCitizenApplicantCohabitant(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantMovingTo findCitizenApplicantMovingTo(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.account.citizen.data.CitizenApplicantPutChildren findCitizenApplicantPutChildren(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public Commune findCommuneByCommunePK(java.lang.Object p0)throws javax.ejb.FinderException,com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public java.lang.String getAcceptMessageSubject() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.account.citizen.data.CitizenAccount getAccount(int p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.Collection getAllAcceptedApplications()throws java.rmi.RemoteException;
 public java.util.Collection getAllPendingApplications()throws java.rmi.RemoteException;
 public java.util.Collection getAllRejectedApplications()throws java.rmi.RemoteException;
 public java.util.List getListOfUnapprovedApplications() throws java.rmi.RemoteException;
 public int getNumberOfApplications()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.String getRejectMessageSubject() throws java.rmi.RemoteException;
 public com.idega.user.data.User getUser(java.lang.String p0) throws java.rmi.RemoteException;
 public com.idega.user.data.User getUserIcelandic(java.lang.String p0) throws java.rmi.RemoteException;
 public java.lang.Integer insertApplication(com.idega.presentation.IWContext p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5)throws com.idega.core.accesscontrol.business.UserHasLoginException,java.rmi.RemoteException, java.rmi.RemoteException;
 public java.lang.Integer insertApplication(com.idega.presentation.IWContext p0,com.idega.user.data.User p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5, boolean p6)throws com.idega.core.accesscontrol.business.UserHasLoginException,java.rmi.RemoteException, java.rmi.RemoteException;
	Integer insertApplication (com.idega.presentation.IWContext iwc, String name, String ssn, String email, String phoneHome, String phoneWork, String careOf, String street, String zipCode, String city, String civilStatus, boolean hasCohabitant, int childrenCount, String applicationReason) throws java.rmi.RemoteException;
 public java.lang.Integer insertChildren(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3)throws java.rmi.RemoteException;
 public java.lang.Integer insertCohabitant(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5)throws java.rmi.RemoteException;
 public java.lang.Integer insertMovingTo(java.lang.Integer p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,java.lang.String p6,java.lang.String p7)throws java.rmi.RemoteException;
 public java.lang.Integer insertPutChildren(java.lang.Integer p0,java.lang.String p1)throws java.rmi.RemoteException;
 public void rejectApplication(int p0,com.idega.user.data.User p1,java.lang.String p2)throws java.rmi.RemoteException,javax.ejb.CreateException,javax.ejb.FinderException, java.rmi.RemoteException;
 public void removeApplication(int p0,com.idega.user.data.User p1)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
}
