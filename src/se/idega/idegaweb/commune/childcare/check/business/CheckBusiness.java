package se.idega.idegaweb.commune.childcare.check.business;

import javax.ejb.*;

public interface CheckBusiness extends com.idega.business.IBOService {
	public void saveCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.lang.Exception, java.rmi.RemoteException;
	public java.lang.String getUserPostalCode(com.idega.presentation.IWContext p0, com.idega.user.data.User p1) throws java.rmi.RemoteException;
	public java.util.Collection findChecks() throws java.lang.Exception, java.rmi.RemoteException;
	public java.util.Collection findUnhandledChecks() throws java.lang.Exception, java.rmi.RemoteException;
	public void createCheck(int p0, int p1, int p2, java.lang.String p3, java.lang.String p4, java.lang.String p5, int p6, int p7, int p8, int p9, com.idega.user.data.User p10, java.lang.String p11, boolean p12, boolean p13, boolean p14, boolean p15, boolean p16) throws java.lang.Exception, java.rmi.RemoteException;
	public se.idega.idegaweb.commune.childcare.check.data.Check getCheck(int p0) throws java.lang.Exception, java.rmi.RemoteException;
	public se.idega.idegaweb.commune.childcare.check.data.Check saveCheckRules(int p0, java.lang.String[] p1, java.lang.String p2, int p3) throws java.lang.Exception, java.rmi.RemoteException;
	public com.idega.core.data.Address getUserAddress(com.idega.presentation.IWContext p0, com.idega.user.data.User p1) throws java.rmi.RemoteException;
	public void commit(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.lang.Exception, java.rmi.RemoteException;
	public void approveCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.lang.Exception, java.rmi.RemoteException;
	public com.idega.user.data.User getUserById(com.idega.presentation.IWContext p0, int p1) throws java.lang.Exception, java.rmi.RemoteException;
	public void retrialCheck(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.lang.Exception, java.rmi.RemoteException;
	public com.idega.block.school.data.SchoolType getSchoolType(com.idega.presentation.IWContext p0, int p1) throws java.lang.Exception, java.rmi.RemoteException;
	public com.idega.user.data.User getUserByPersonalId(com.idega.presentation.IWContext p0, java.lang.String p1) throws java.lang.Exception, java.rmi.RemoteException;
	public void sendMessageToCitizen(com.idega.presentation.IWContext p0, se.idega.idegaweb.commune.childcare.check.data.Check p1, int p2, java.lang.String p3, java.lang.String p4) throws java.lang.Exception, java.rmi.RemoteException;
	public void sendMessageToPrinter(com.idega.presentation.IWContext p0, se.idega.idegaweb.commune.childcare.check.data.Check p1, int p2, java.lang.String p3, java.lang.String p4) throws java.lang.Exception, java.rmi.RemoteException;
	public void sendMessageToArchive(com.idega.presentation.IWContext p0, se.idega.idegaweb.commune.childcare.check.data.Check p1, int p2, java.lang.String p3, java.lang.String p4) throws java.lang.Exception, java.rmi.RemoteException;
	public boolean allRulesVerified(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.lang.Exception;
	public int getUserID(se.idega.idegaweb.commune.childcare.check.data.Check p0) throws java.rmi.RemoteException;
}
