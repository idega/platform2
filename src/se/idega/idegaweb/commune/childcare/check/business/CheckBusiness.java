package se.idega.idegaweb.commune.childcare.check.business;

import javax.ejb.*;

public interface CheckBusiness extends com.idega.business.IBOService
{
 public void saveCheck()throws java.lang.Exception, java.rmi.RemoteException;
 public java.lang.String getUserPostalCode(com.idega.presentation.IWContext p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public java.util.Collection findChecks()throws java.lang.Exception, java.rmi.RemoteException;
 public boolean isRule5Verified() throws java.rmi.RemoteException;
 public void createCheck(int p0,int p1,int p2,java.lang.String p3,java.lang.String p4,java.lang.String p5,int p6,int p7,int p8,int p9,int p10,java.lang.String p11,boolean p12,boolean p13,boolean p14,boolean p15,boolean p16)throws java.lang.Exception, java.rmi.RemoteException;
 public boolean isRule3Verified() throws java.rmi.RemoteException;
 public boolean isRule2Verified() throws java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check getCheck(int p0)throws java.lang.Exception, java.rmi.RemoteException;
 public void verifyCheckRules(int p0,java.lang.String[] p1,java.lang.String p2,int p3)throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.core.data.Address getUserAddress(com.idega.presentation.IWContext p0,com.idega.user.data.User p1) throws java.rmi.RemoteException;
 public boolean allRulesVerified() throws java.rmi.RemoteException;
 public void commit()throws java.lang.Exception, java.rmi.RemoteException;
 public se.idega.idegaweb.commune.childcare.check.data.Check getCurrentCheck()throws java.lang.Exception, java.rmi.RemoteException;
 public void approveCheck()throws java.lang.Exception, java.rmi.RemoteException;
 public com.idega.user.data.User getUserById(com.idega.presentation.IWContext p0,int p1)throws java.lang.Exception, java.rmi.RemoteException;
 public boolean isRule4Verified() throws java.rmi.RemoteException;
 public void retrialCheck()throws java.lang.Exception, java.rmi.RemoteException;
 public boolean isRule1Verified() throws java.rmi.RemoteException;
 public com.idega.user.data.User getUserByPersonalId(com.idega.presentation.IWContext p0,java.lang.String p1)throws java.lang.Exception, java.rmi.RemoteException;
 public void sendMessageToCitizen(java.lang.String p0,java.lang.String p1,int p2)throws java.lang.Exception, java.rmi.RemoteException;
}
